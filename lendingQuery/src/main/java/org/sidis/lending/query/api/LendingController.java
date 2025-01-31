package org.sidis.lending.query.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sidis.lending.query.api.LendingView;
import org.sidis.lending.query.exceptions.NotFoundException;
import org.sidis.lending.query.model.Lending;
import org.sidis.lending.query.repositories.LendingRepository;
import org.sidis.lending.query.service.CreateLendingRequest;
import org.sidis.lending.query.service.EditLendingRequest;
import org.sidis.lending.query.service.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Tag(name = "Lendings", description = "Endpoints for managing Lendings")
@RestController
@RequestMapping("/api/lendings")
public class LendingController {

    private final LendingService service;
    private final LendingViewMapper lendingViewMapper;

    @Autowired
    public LendingController(LendingService service, LendingViewMapper lendingViewMapper) {
        this.service = service;
        this.lendingViewMapper = lendingViewMapper;
    }

    @Operation(summary = "Gets all lendings")
    @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = LendingView.class)))
    })
    @GetMapping
    public Iterable<LendingView> findAll() {
        return lendingViewMapper.toLendingView(service.findAll());
    }

    @Operation(summary = "Gets a specific lending")
    @GetMapping(value = "/{id1}/{id2}")
    public ResponseEntity<LendingView> findById(@PathVariable("id1") final int id1, @PathVariable("id2") final int id2) {
        final var lending = service.findById(id1, id2)
                .orElseThrow(() -> new NotFoundException(Lending.class, id1 + "/" + id2));
        return ResponseEntity.ok().eTag(Long.toString(lending.getVersion())).body(lendingViewMapper.toLendingView(lending));
    }

    @Operation(summary = "Lists overdue lendings sorted by their tardiness")
    @GetMapping("/overdue")
    public ResponseEntity<List<LendingView>> listOverdueLendingsSortedByTardiness() {
        List<Lending> overdueLendings = service.getOverdueLendingsSortedByTardiness();
        List<LendingView> lendingViews = lendingViewMapper.toLendingView(overdueLendings);
        return ResponseEntity.ok(lendingViews);
    }

    @Operation(summary = "Gets the average lending duration")
    @GetMapping("/average-lending-duration")
    public ResponseEntity<Double> getAverageLendingDuration() {
        double avgDuration = service.getAverageLendingDuration();
        return ResponseEntity.ok(avgDuration);
    }

    @Operation(summary = "Gets the average number of lending per genre of a certain month")
    @GetMapping("/average-lending-per-genre")
    public ResponseEntity<Map<String, Double>> getAverageLendingDurationPerGenreAndMonth(
            @RequestParam int month, @RequestParam int year) {
        Map<String, Double> result = service.getAverageLendingsPerGenre(month, year);
        return ResponseEntity.ok(result);
    }
}
