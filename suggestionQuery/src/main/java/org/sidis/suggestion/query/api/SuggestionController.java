package org.sidis.suggestion.query.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sidis.suggestion.query.api.SuggestionView;
import org.sidis.suggestion.query.api.SuggestionViewMapper;
import org.sidis.suggestion.query.exceptions.NotFoundException;
import org.sidis.suggestion.query.model.Suggestion;
import org.sidis.suggestion.query.repositories.SuggestionRepository;
import org.sidis.suggestion.query.service.CreateSuggestionRequest;
import org.sidis.suggestion.query.service.EditSuggestionRequest;
import org.sidis.suggestion.query.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Suggestions", description = "Endpoints for managing Suggestions")
@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {

    private final SuggestionService service;
    private final SuggestionViewMapper suggestionViewMapper;
    private final SuggestionRepository repository;

    @Autowired
    public SuggestionController(SuggestionService service, SuggestionViewMapper suggestionViewMapper, SuggestionRepository repository) {
        this.service = service;
        this.suggestionViewMapper = suggestionViewMapper;
        this.repository = repository;
    }

    @Operation(summary = "Gets all suggestions")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = SuggestionView.class))) })
    @GetMapping
    public Iterable<SuggestionView> findAll() {return suggestionViewMapper.toSuggestionView(service.findAll());}


    @Operation(summary = "Gets a specific Suggestion by id")
    @GetMapping(value = "/id/{id1}/{id2}")
    public ResponseEntity<SuggestionView> findBySuggestionID(
            @PathVariable("id1") @Parameter(description = "The id of the suggestion to find") final String id1,
            @PathVariable("id2") final String id2) {
        String suggestionID = id1 + "/" + id2;
        final var suggestion = service.getSuggestionByID(suggestionID).orElseThrow(() -> new NotFoundException(Suggestion.class, suggestionID));
        return ResponseEntity.ok().eTag(Long.toString(suggestion.getVersion())).body(suggestionViewMapper.toSuggestionView(suggestion));
    }

    @Operation(summary = "Gets all suggestions by isbn")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = SuggestionView.class))) })
    @GetMapping(value = "/isbn/{isbn}")
    public Iterable<SuggestionView> findSuggestionByISBN(@PathVariable("isbn") @Parameter(description = "The isbn of the suggestion to find") final String isbn){
        return suggestionViewMapper.toSuggestionView(service.getSuggestionByISBN(isbn));
    }

    @Operation(summary = "Gets all suggestions by readerID")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            array = @ArraySchema(schema = @Schema(implementation = SuggestionView.class))) })
    @GetMapping(value = "/reader/{id1}/{id2}")
    public Iterable<SuggestionView> findSuggestionByReaderID(
            @PathVariable("id1") @Parameter(description = "The id of the reader to find") final String id1,
            @PathVariable("id2") final String id2) {
        String readerID = id1 + "/" + id2;
        return suggestionViewMapper.toSuggestionView(service.getSuggestionByReaderID(readerID));
    }
}
