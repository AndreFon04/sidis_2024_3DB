package org.sidis.lending.command.service;

import org.sidis.lending.command.model.Lending;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface LendingService {


    Optional<String> findReaderByLendingID(String lendingID);  // Retorna apenas o ID do Reader
    Optional<Lending> getLastId();
    List<Lending> getOverdueLendingsSortedByTardiness();
    Lending create(CreateLendingRequest request);

    Lending partialUpdate(int id1, int id2, EditLendingRequest resource, long desiredVersion);

    int calculateFine(String lendingID);

    double getAverageLendingDuration();

    Map<String, Double> getAverageLendingsPerGenre(int month, int year);
}
