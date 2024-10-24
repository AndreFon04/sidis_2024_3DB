package org.sidis.lendings.lendingmanagement.services;

import com.example.library.bookmanagement.model.Book;
import com.example.library.bookmanagement.repositories.BookRepository;
import com.example.library.exceptions.NotFoundException;
import com.example.library.lendingmanagement.config.LendingConfiguration;
import com.example.library.lendingmanagement.model.Lending;
import com.example.library.lendingmanagement.repositories.LendingRepository;
import com.example.library.readermanagement.model.Reader;
import com.example.library.readermanagement.repositories.ReaderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LendingServiceImpl implements LendingService {

    private final LendingRepository lendingRepository;
    private final LendingConfiguration lendingConfig;
    private final BookRepository bookRepository;
    private final ReaderRepository readerRepository;

    public LendingServiceImpl(@Qualifier("springDataLendingRepository") LendingRepository lendingRepository,
                              @Qualifier("springDataBookRepository") BookRepository bookRepository,
                              @Qualifier("springDataReaderRepository") ReaderRepository readerRepository,
                              LendingConfiguration lendingConfig) {
        this.lendingRepository = lendingRepository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.lendingConfig = lendingConfig;
    }

    private static final Logger logger = LoggerFactory.getLogger(LendingServiceImpl.class);
    @Override
    public Iterable<Lending> findAll() {
        return lendingRepository.findAll();
    }

    @Override
    public Optional<Lending> findById(int id1, int id2) {
        String lendingID = id1 + "/" + id2;
        return lendingRepository.findByLendingID(lendingID);
    }

    @Override
    public Optional<Reader> findReaderByLendingId(String lendingID) {
        return lendingRepository.findReaderByLendingId(lendingID);
    }

    @Override
    public Optional<Lending> getLastId() {
        return lendingRepository.findTopByOrderByLendingIDDesc();
    }

    @Override
    public List<Lending> getOverdueLendingsSortedByTardiness() {
        return lendingRepository.findByOverdueTrueOrderByTardinessDesc();
    }

    @Override
    public Lending create(CreateLendingRequest request) {
        Book book = bookRepository.findById(request.getBookID())
                .orElseThrow(() -> new NotFoundException("Book not found"));
        Reader reader = readerRepository.findByReaderID(request.getReaderID())
                .orElseThrow(() -> new NotFoundException("Reader not found"));

        boolean hasOverdueLending = lendingRepository.existsByReader_ReaderIDAndIsOverdueTrue(reader.getReaderID());
        if (hasOverdueLending) {
            throw new IllegalArgumentException("Reader has overdue lending and cannot borrow more books.");
        }

        long activeLendingsCount = lendingRepository.countActiveLendingsByReader_ReaderID(reader.getReaderID());
        if (activeLendingsCount >= 3) {
            throw new IllegalArgumentException("Reader already has the maximum number of active lendings (3).");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate expectedReturnDate = startDate.plusDays(lendingConfig.getMaxDaysWithoutFine());
        LocalDate returnDate = null;
        int fine = 0;
        boolean overdue = false;

        Lending newLending = new Lending(book, reader, startDate, returnDate, expectedReturnDate, overdue, fine);
        newLending.updateOverdueStatus();
        return lendingRepository.save(newLending);
    }

    @Override
    public Lending partialUpdate(int id1, int id2, EditLendingRequest resource, long desiredVersion) {
        String lendingID = id1 + "/" + id2;
        Lending lending = lendingRepository.findByLendingID(lendingID)
                .orElseThrow(() -> new NotFoundException("Lending record not found."));

        if (resource.getReturnDate() != null) {
            lending.setReturnDate(resource.getReturnDate());
            lending.updateOverdueStatus();
            lending.setFine(calculateFine(lending));
            if (resource.getReturnDate().isAfter(lending.getExpectedReturnDate())) {
                lending.setOverdue(true);
            } else {
                lending.setOverdue(false);
            }
        }

        lending.setNotes(resource.getNotes());
        return lendingRepository.save(lending);
    }

    private int calculateFine(Lending lending) {
        LocalDate expectedReturnDate = lending.getExpectedReturnDate();
        LocalDate returnDate = lending.getReturnDate();

        if (returnDate != null && returnDate.isAfter(expectedReturnDate)) {
            long daysLate = ChronoUnit.DAYS.between(expectedReturnDate, returnDate);
            return (int) (daysLate * lendingConfig.getFinePerDay());
        } else {
            return 0;
        }
    }

    @Override
    public int calculateFine(String lendingID) {
        Optional<Lending> optionalLending = lendingRepository.findByLendingID(lendingID);
        if (optionalLending.isEmpty()) {
            return 0;
        }

        Lending lending = optionalLending.get();
        return calculateFine(lending);
    }

    @Override
    public Map<String, Double> getAverageLendingPerGenreForMonth(int month, int year) {
        List<Object[]> results = lendingRepository.findLendingsCountByGenreAndMonth(month, year);
        int daysInMonth = YearMonth.of(year, month).lengthOfMonth();

        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> {
                            BigDecimal count = BigDecimal.valueOf((long) result[1]);
                            BigDecimal average = count.divide(BigDecimal.valueOf(daysInMonth), 1, RoundingMode.HALF_UP);
                            return average.doubleValue();
                        }
                ));
    }

    @Override
    public double getAverageLendingDuration() {
        double avgDuration = lendingRepository.findAverageLendingDuration();
        System.out.println("Raw average lending duration: " + avgDuration);
        BigDecimal formattedDuration = BigDecimal.valueOf(avgDuration).setScale(1, RoundingMode.HALF_UP);
        System.out.println("Formatted average lending duration: " + formattedDuration.doubleValue());
        return formattedDuration.doubleValue();
    }

    @Override
    public Map<String, Map<String, Long>> getLendingsPerMonthByGenreForLastYear() {
        YearMonth currentMonth = YearMonth.now();
        YearMonth startMonth = currentMonth.minusMonths(11);
        LocalDate startDate = startMonth.atDay(1);

        Map<String, Map<String, Long>> lendingsPerMonthByGenre = new HashMap<>();

        List<Object[]> results = lendingRepository.findLendingsCountByGenreAndMonth(startDate);

        for (Object[] result : results) {
            String genre = (String) result[0];
            Long count = (Long) result[1];
            int month = (int) result[2];
            int year = (int) result[3];

            String yearMonth = String.format("%d-%02d", year, month);

            lendingsPerMonthByGenre
                    .computeIfAbsent(yearMonth, k -> new HashMap<>())
                    .put(genre, count);
        }

        for (YearMonth month = startMonth; !month.isAfter(currentMonth); month = month.plusMonths(1)) {
            String yearMonth = month.toString();
            lendingsPerMonthByGenre.putIfAbsent(yearMonth, new HashMap<>());
        }

        return lendingsPerMonthByGenre;
    }

    @Override
    public long getLendingCountByReaderForMonth(String readerID, int month, int year) {
        return lendingRepository.countLendingsByReaderAndMonth(readerID, month, year);
    }

    @Override
    public Map<String, Double> getAverageLendingDurationPerGenre(int month, int year) {
        List<Object[]> results = lendingRepository.findAverageLendingDurationPerGenreAndMonth(month, year);

        Map<String, Double> averageDurations = new HashMap<>();

        for (Object[] result : results) {
            String genre = (String) result[0];
            Double avgDuration = (Double) result[1];

            BigDecimal roundedAvgDuration = BigDecimal.valueOf(avgDuration).setScale(1, RoundingMode.HALF_UP);

            averageDurations.put(genre, roundedAvgDuration.doubleValue());
        }

        return averageDurations;
    }

    @Override
    public Map<String, Double> getAverageLendingDurationPerBook() {
        List<Object[]> results = lendingRepository.findAverageLendingDurationPerBook();
        Map<String, Double> averageDurations = new HashMap<>();

        for (Object[] result : results) {
            String bookTitle = (String) result[0];
            Double avgDuration = ((Number) result[1]).doubleValue();

            BigDecimal roundedAvgDuration = BigDecimal.valueOf(avgDuration).setScale(1, RoundingMode.HALF_UP);
            averageDurations.put(bookTitle, roundedAvgDuration.doubleValue());
        }

        return averageDurations;
    }


}
