package org.sidis.userservice.api;

import org.sidis.userservice.client.GenreDTO;
import org.sidis.userservice.exceptions.NotFoundException;
import org.sidis.userservice.model.Reader;
import org.sidis.userservice.model.ReaderCountDTO;
import org.sidis.userservice.repositories.ReaderRepository;
import org.sidis.userservice.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@Tag(name = "Readers", description = "Endpoints for managing readers")
@RestController
@RequestMapping("/api/readers")
class ReaderController {

    private static final String IF_MATCH = "If-Match";
    private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);

    private final ReaderServiceImpl readerService;
    private final ReaderViewMapper readerMapper;
    private final ReaderRepository readerRepository;

    @Autowired
    public ReaderController(ReaderServiceImpl readerService, ReaderViewMapper readerMapper, ReaderRepository readerRepo) {
        this.readerService = readerService;
        this.readerMapper = readerMapper;
        this.readerRepository = readerRepo;
    }

    @Operation(summary = "Gets all readers")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = ReaderView.class))) })
    @GetMapping
    public Iterable<ReaderView> findAll() {
        return readerMapper.toReaderView(readerService.findAll());
    }

    @Operation(summary = "Gets a specific Reader by id")
    @GetMapping(value = "/id/{id1}/{id2}")
    public ResponseEntity<ReaderView> findById(
            @PathVariable("id1") @Parameter(description = "The id of the reader to find") final String id1,
            @PathVariable("id2") final String id2) {
        String readerID = id1 + "/" + id2;
        final var reader = readerService.getReaderByID(readerID).orElseThrow(() -> new NotFoundException(Reader.class, readerID));
        return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(readerMapper.toReaderView(reader));
    }

    @Operation(summary = "Gets a specific Reader by email")
    @GetMapping(value = "/email/{email}")
    public ResponseEntity<ReaderView> findByEmail(
            @PathVariable("email") @Parameter(description = "The email of the reader to find") final String email) {
        Reader reader = readerService.getReaderByEmail(email).orElseThrow(() -> new NotFoundException(Reader.class, email));
        return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(readerMapper.toReaderView(reader));
    }

    @Operation(summary = "Gets the top 5 Readers by number of lendings")
    @GetMapping("/top5")
    public List<ReaderCountDTO> findTop5() {
        return readerService.findTop5Readers();
    }

    @Operation(summary = "Gets book suggestions based on the list of interests of the Reader")
    @GetMapping(value = "/suggestions/{id1}/{id2}")
    public List<GenreDTO> findSuggestions(@PathVariable("id1") final String id1, @PathVariable("id2") final String id2) {
        String readerID = id1 + "/" + id2;
        Reader reader = readerService.getReaderByID(readerID).orElseThrow(() -> new NotFoundException(Reader.class, readerID));

        return readerService.getBookSuggestions(reader);
    }
    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }


    @Operation(summary = "Searches any existing Reader by name, email or phone number")
    @PostMapping("search")
    public ListResponse<ReaderView> search(@RequestBody final SearchRequest<SearchReadersQuery> request) {
        final List<Reader> searchReaders = readerService.searchReaders(request.getPage(), request.getQuery());
        return new ListResponse<>(readerMapper.toReaderView(searchReaders));
    }



    @Operation(summary = "Partially updates an existing reader")
    @PatchMapping(value = "/{id1}/{id2}")
    public ResponseEntity<ReaderView> partialUpdate(final WebRequest request,
                                                    @PathVariable("id1") final String id1, @PathVariable("id2") final String id2,
                                                    @Valid @RequestBody final EditReaderRequest resource) {
        String readerID = id1 + "/" + id2;

        // Validar se o user autenticado tem o mesmo readerID que o readerID acima, se nao, é FORBIDDEN
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        currentUsername = currentUsername.replaceFirst(".*,", ""); //PREGO

        Reader r = readerService.getReaderByID(readerID).orElseThrow(() -> new NotFoundException(Reader.class, readerID));

        System.out.println(readerID + " " + currentUsername);
        System.out.println(r.getReaderID() + " " + r.getEmail());

        if (!currentUsername.equals(r.getEmail())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this object.");
        }
        // Fim de validação

        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must issue a conditional PATCH using 'if-match'");
        }

        final var reader = readerService.partialUpdate(readerID, resource, getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(readerMapper.toReaderView(reader));
    }

    @PostMapping("/internal")
    public ResponseEntity<Reader> saveReader(@Valid @RequestBody final Reader request) {
        Reader reader = readerService.saveReader(request);

        return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(reader);
    }


}
