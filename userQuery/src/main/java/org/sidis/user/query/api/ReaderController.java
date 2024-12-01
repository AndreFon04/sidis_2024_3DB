package org.sidis.user.query.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sidis.user.query.api.ListResponse;
import org.sidis.user.query.api.ReaderView;
import org.sidis.user.query.api.ReaderViewMapper;
import org.sidis.user.query.client.GenreDTO;
import org.sidis.user.query.exceptions.NotFoundException;
import org.sidis.user.query.model.Reader;
import org.sidis.user.query.model.ReaderCountDTO;
import org.sidis.user.query.repositories.ReaderRepository;
import org.sidis.user.query.service.*;
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

//    @Operation(summary = "Gets the top 5 Readers by number of lendings")
//    @GetMapping("/top5")
//    public List<ReaderCountDTO> findTop5() {
//        return readerService.findTop5Readers();
//    }

//    @Operation(summary = "Gets book suggestions based on the list of interests of the Reader")
//    @GetMapping(value = "/suggestions/{id1}/{id2}")
//    public List<GenreDTO> findSuggestions(@PathVariable("id1") final String id1, @PathVariable("id2") final String id2) {
//        String readerID = id1 + "/" + id2;
//        Reader reader = readerService.getReaderByID(readerID).orElseThrow(() -> new NotFoundException(Reader.class, readerID));
//
//        return readerService.getBookSuggestions(reader);
//    }
}
