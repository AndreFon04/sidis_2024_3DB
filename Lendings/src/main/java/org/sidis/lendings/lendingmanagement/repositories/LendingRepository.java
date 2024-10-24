package org.sidis.lendings.lendingmanagement.repositories;

import com.example.library.lendingmanagement.model.Lending;
import com.example.library.readermanagement.model.Reader;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LendingRepository {

	Optional<Lending> findByLendingID(String lendingID);

	@NotNull Iterable<Lending> findAll();

	Optional<Reader> findReaderByLendingId(String lendingID);

	Optional<Lending> findTopByOrderByLendingIDDesc();

	@NotNull Lending save(@NotNull Lending newLending);

	long countActiveLendingsByReader_ReaderID(String readerID);

	boolean existsByReader_ReaderIDAndIsOverdueTrue(String readerID);

	List<Lending> findByOverdueTrueOrderByTardinessDesc();

	List<Object[]> findLendingsCountByGenreAndMonth(int month, int year);

	double findAverageLendingDuration();

	List<Object[]> findLendingsCountByGenreAndMonth(LocalDate startDate);

	List<Object[]> findAverageLendingDurationPerGenreAndMonth(int month, int year);

	long countLendingsByReaderAndMonth(String readerID, int month, int year);

	List<Object[]> findAverageLendingDurationPerBook();

	Optional<Lending> findByBook_BookIDAndReader_ReaderIDAndStartDateAndExpectedReturnDate(Long bookID, String readerID, LocalDate startDate, LocalDate expectedReturnDate);

}

