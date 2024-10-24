package org.sidis.lendings.lendingmanagement.infrastructure.repositories.impl;

import com.example.library.lendingmanagement.model.Lending;
import com.example.library.lendingmanagement.repositories.LendingRepository;
import com.example.library.readermanagement.model.Reader;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataLendingRepository extends CrudRepository<Lending, Long>, LendingRepository {

	@Override
	@Query("SELECT l FROM Lending l WHERE l.lendingID = :lendingID")
	Optional<Lending> findByLendingID(@Param("lendingID") String lendingID);

	@Override
	@Query("SELECT l FROM Lending l")
	Iterable<Lending> findAll();

	@Override
	@Query(value = "SELECT l.* FROM Lending l WHERE l.lendingID = l.lendingID ", nativeQuery = true)
	Optional<Reader> findReaderByLendingId(@Param("lendingID") String lendingID);

	@Override
	@Query("SELECT l FROM Lending l ORDER BY substring(l.lendingID, 1, 4), cast(substring(l.lendingID, 6, 10) AS int) DESC LIMIT 1")
	Optional<Lending> findTopByOrderByLendingIDDesc();

	@Override
	@Query("SELECT COUNT(l) FROM Lending l WHERE l.reader.readerID = :readerID AND l.returnDate IS NULL")
	long countActiveLendingsByReader_ReaderID(@Param("readerID") String readerID);

	@Override
	@Query("SELECT COUNT(l) > 0 FROM Lending l WHERE l.reader.readerID = :readerID AND l.overdue = true AND l.returnDate IS NULL")
	boolean existsByReader_ReaderIDAndIsOverdueTrue(@Param("readerID") String readerID);

	@Override
	@Query("SELECT l FROM Lending l WHERE l.overdue = true AND l.returnDate IS NULL ORDER BY FUNCTION('timestampdiff', DAY, l.expectedReturnDate, CURRENT_DATE) DESC")
	List<Lending> findByOverdueTrueOrderByTardinessDesc();

	@Query("SELECT l.book.genre.interest, COUNT(l) FROM Lending l WHERE MONTH(l.startDate) = :month AND YEAR(l.startDate) = :year GROUP BY l.book.genre.interest")
	List<Object[]> findLendingsCountByGenreAndMonth(@Param("month") int month, @Param("year") int year);

	@Query("SELECT l.book.genre.interest, COUNT(l), MONTH(l.startDate), YEAR(l.startDate) " +
			"FROM Lending l WHERE l.startDate >= :startDate " +
			"GROUP BY l.book.genre.interest, MONTH(l.startDate), YEAR(l.startDate) " +
			"ORDER BY YEAR(l.startDate), MONTH(l.startDate)")
	List<Object[]> findLendingsCountByGenreAndMonth(@Param("startDate") LocalDate startDate);

	@Query("SELECT AVG(FUNCTION('timestampdiff', DAY, l.startDate, l.returnDate)) FROM Lending l WHERE l.returnDate IS NOT NULL")
	double findAverageLendingDuration();

	@Query("SELECT COUNT(l) FROM Lending l WHERE l.reader.readerID = :readerID AND MONTH(l.startDate) = :month AND YEAR(l.startDate) = :year")
	long countLendingsByReaderAndMonth(@Param("readerID") String readerID, @Param("month") int month, @Param("year") int year);

	@Query("SELECT l.book.genre.interest as genre, AVG(FUNCTION('timestampdiff', DAY, l.startDate, l.returnDate)) as avgDuration " +
			"FROM Lending l " +
			"WHERE MONTH(l.startDate) = :month AND YEAR(l.startDate) = :year AND l.returnDate IS NOT NULL " +
			"GROUP BY l.book.genre.interest " +
			"ORDER BY l.book.genre.interest")
	List<Object[]> findAverageLendingDurationPerGenreAndMonth(@Param("month") int month, @Param("year") int year);

	@Query("SELECT l.book.title, AVG(FUNCTION('timestampdiff', DAY, l.startDate, l.returnDate)) FROM Lending l WHERE l.returnDate IS NOT NULL GROUP BY l.book.title")
	List<Object[]> findAverageLendingDurationPerBook();



}
