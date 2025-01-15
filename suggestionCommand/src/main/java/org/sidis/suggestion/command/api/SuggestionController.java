package org.sidis.suggestion.command.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sidis.suggestion.command.service.SuggestionService;
import org.sidis.suggestion.command.service.CreateSuggestionRequest;
import org.sidis.suggestion.command.service.EditSuggestionRequest;
import org.sidis.suggestion.command.repositories.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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


    @Operation(summary = "Creates a new suggestion")
    @PostMapping
    public ResponseEntity<SuggestionView> create(@RequestBody @Valid CreateSuggestionRequest request) {
        final var suggestion = service.create(request);
        return ResponseEntity.ok(suggestionViewMapper.toSuggestionView(suggestion));
    }

    @Operation(summary = "Partially updates an existing suggestion")
    @PatchMapping(value = "/{id}")
    public ResponseEntity<SuggestionView> partialUpdate(@PathVariable("id") final int id,
                                                        @Valid @RequestBody final EditSuggestionRequest resource) {
        final var suggestion = service.partialUpdate(id, resource, 1L);
        return ResponseEntity.ok().body(suggestionViewMapper.toSuggestionView(suggestion));
    }

}
