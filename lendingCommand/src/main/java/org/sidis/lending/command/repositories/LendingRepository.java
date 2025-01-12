package org.sidis.lending.command.repositories;

import org.jetbrains.annotations.NotNull;
import org.sidis.lending.command.model.Lending;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Repository
public interface LendingRepository {

    Optional<Lending> findByLendingID(String lendingID);
    void delete(Lending lending);

    Optional<Lending> findBookByISBN(String isbn);

    @NotNull Iterable<Lending> findAll();

    @NotNull Lending save(@NotNull Lending newLending);

    Optional<String> findReaderByLendingID(String lendingID);

    List<Lending> findByOverdueTrueOrderByTardinessDesc();

    long countActiveLendingsByReaderID(String readerID);

    boolean existsByReaderIDAndOverdueTrue(String readerID);

    List<Lending> findByOverdueTrueOrderByExpectedReturnDateDesc();
    List<Object[]> findLendingsDurationByBookAndMonth(int month, int year);

    Optional<Lending> findFirstByOrderByLendingIDDesc();

    List<Lending> findLendingsWithReturnDate();

    long countLendingsByReaderIDAndMonth(String readerID, int month, int year);

    Optional<Lending> findByBookIDAndReaderIDAndStartDateAndExpectedReturnDate(Long bookID, String readerID, LocalDate startDate, LocalDate expectedReturnDate);

}
