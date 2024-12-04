package org.sidis.lending.command.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sidis.lending.command.exceptions.NotFoundException;
import org.sidis.lending.command.model.Lending;
import org.sidis.lending.command.repositories.LendingRepository;
import org.sidis.lending.command.service.CreateLendingRequest;
import org.sidis.lending.command.service.EditLendingRequest;
import org.sidis.lending.command.service.LendingService;
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
    private final LendingRepository lendingRepository;

    @Autowired
    public LendingController(LendingService service, LendingViewMapper lendingViewMapper, LendingRepository lendingRepository) {
        this.service = service;
        this.lendingViewMapper = lendingViewMapper;
        this.lendingRepository = lendingRepository;
    }

    @Operation(summary = "Creates a new lending")
    @PostMapping
    public ResponseEntity<LendingView> create(@RequestBody @Valid CreateLendingRequest request) {
        System.out.println("entered post lending: " + request.getReaderID() + ", " + request.getBookID());
        final var lending = service.create(request);
        return ResponseEntity.ok(lendingViewMapper.toLendingView(lending));
    }

    @Operation(summary = "Partially updates an existing lending")
    @PatchMapping(value = "/{id1}/{id2}")
    public ResponseEntity<LendingView> partialUpdate(@PathVariable("id1") final int id1, @PathVariable("id2") final int id2,
                                                     @Valid @RequestBody final EditLendingRequest resource) {
        final var lending = service.partialUpdate(id1, id2, resource, 1L);
        return ResponseEntity.ok().body(lendingViewMapper.toLendingView(lending));
    }

    @PostMapping("/sync")
    public ResponseEntity<Lending> createLendingSync(@RequestBody Lending lending) {
        Optional<Lending> existingLending = lendingRepository.findByLendingID(lending.getLendingID());

        if (existingLending.isPresent()) {
            Lending existing = existingLending.get();
            // Atualiza os campos modificados, sem validar a versão
            existing.setBookID(lending.getBookID());
            existing.setReaderID(lending.getReaderID());
            existing.setStartDate(lending.getStartDate());
            existing.setExpectedReturnDate(lending.getExpectedReturnDate());
            existing.setReturnDate(lending.getReturnDate());
            existing.setOverdue(lending.isOverdue());
            existing.setFine(lending.getFine());
            existing.setNotes(lending.getNotes());
            lending.updateOverdueStatus();

            existing.setVersion(lending.getVersion());

            Lending updatedLending = lendingRepository.save(existing); // Atualiza a entidade
            return ResponseEntity.ok(updatedLending);
        } else {
            // Se o empréstimo não existir, cria um novo
            Lending savedLending = lendingRepository.save(lending);
            return ResponseEntity.ok(savedLending);
        }
    }
}
