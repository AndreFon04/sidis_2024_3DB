package org.sidis.user.command.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sidis.user.command.exceptions.NotFoundException;
import org.sidis.user.command.model.Reader;
import org.sidis.user.command.repositories.ReaderRepository;
import org.sidis.user.command.service.*;
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


@Tag(name = "Readers", description = "Endpoints for managing readers")
@RestController
@RequestMapping("/api/readers")
class ReaderController {

    private static final String IF_MATCH = "If-Match";
    private static final Logger logger = LoggerFactory.getLogger(ReaderController.class);

    private final ReaderServiceImpl readerService;
    private final ReaderViewMapper readerMapper;

    @Autowired
    public ReaderController(ReaderServiceImpl readerService, ReaderViewMapper readerMapper) {
        this.readerService = readerService;
        this.readerMapper = readerMapper;
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

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }

//    @PostMapping("/internal")
//    public ResponseEntity<Reader> saveReader(@Valid @RequestBody final Reader request) {
//        Reader reader = readerService.saveReader(request);
//
//        return ResponseEntity.ok().eTag(Long.toString(reader.getVersion())).body(reader);
//    }
}
