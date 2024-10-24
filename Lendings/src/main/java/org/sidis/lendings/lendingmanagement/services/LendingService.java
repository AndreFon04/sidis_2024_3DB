package org.sidis.lendings.lendingmanagement.services;

import com.example.library.lendingmanagement.model.Lending;
import com.example.library.readermanagement.model.Reader;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface LendingService {

    Iterable<Lending> findAll();
    Optional<Lending> findById(int id1, int id2);

    Optional<Reader> findReaderByLendingId(String lendingID);

    Optional<Lending> getLastId();
    List<Lending> getOverdueLendingsSortedByTardiness();
    Lending create(CreateLendingRequest request);
    Lending partialUpdate(int id1, int id2, EditLendingRequest resource, long desiredVersion);

    int calculateFine(String lendingID);

    Map<String, Double> getAverageLendingPerGenreForMonth(int month, int year);

    double getAverageLendingDuration();

    Map<String, Map<String, Long>> getLendingsPerMonthByGenreForLastYear();

    long getLendingCountByReaderForMonth(String readerID, int month, int year);

    Map<String, Double> getAverageLendingDurationPerGenre(int month, int year);

    Map<String, Double> getAverageLendingDurationPerBook();


}
