package org.sidis.lending.query.service;

import org.sidis.lending.query.model.Lending;
import org.sidis.lending.query.service.CreateLendingRequest;
import org.sidis.lending.query.service.EditLendingRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface LendingService {

    Iterable<Lending> findAll();
    Optional<Lending> findById(int id1, int id2);
//    String getOtherInstanceUrl();

    Optional<String> findReaderByLendingID(String lendingID);  // Retorna apenas o ID do Reader
    Optional<Lending> getLastId();
    List<Lending> getOverdueLendingsSortedByTardiness();

    int calculateFine(String lendingID);

    double getAverageLendingDuration();

    Map<String, Double> getAverageLendingsPerGenre(int month, int year);
}
