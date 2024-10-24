package org.sidis.lendings.bootstrapping;

import com.example.library.bookmanagement.model.Book;
import com.example.library.bookmanagement.repositories.BookRepository;
import com.example.library.lendingmanagement.model.Lending;
import com.example.library.lendingmanagement.repositories.LendingRepository;
import com.example.library.readermanagement.model.Reader;
import com.example.library.readermanagement.repositories.ReaderRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Component
@Profile("bootstrap")
@Order(5)
public class LendingBootstrapper implements CommandLineRunner {

    private final LendingRepository lendingRepo;
    private final BookRepository bookRepo;
    private final ReaderRepository readerRepo;

    public LendingBootstrapper(@Qualifier("springDataLendingRepository") LendingRepository lendingRepo,
                               @Qualifier("springDataBookRepository") BookRepository bookRepo,
                               @Qualifier("springDataReaderRepository") ReaderRepository readerRepo) {
        this.lendingRepo = lendingRepo;
        this.bookRepo = bookRepo;
        this.readerRepo = readerRepo;
    }

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        System.out.println("lendingBt.run");

        final Optional<Lending> lastLending = lendingRepo.findTopByOrderByLendingIDDesc();
        lastLending.ifPresent(lending -> System.out.println("Last Lending ID: " + lending.getLendingID()));
        lastLending.ifPresent(lending -> lending.initCounter(lending.getLendingID()));

        // Criar empréstimos já devolvidos (com returnDate)
        createLendingIfNotExists(1L, "2024/1", "2024-02-01", "2024-02-16", "2024-02-16", true, 5);
        createLendingIfNotExists(4L, "2024/22", "2024-03-01", "2024-03-16", "2024-03-16", true, 20);
        createLendingIfNotExists(3L, "2024/21", "2024-04-01", "2024-04-16", "2024-04-16", true, 20);
        createLendingIfNotExists(5L, "2024/2", "2024-02-01", "2024-02-16", "2024-02-16", true, 20);
        createLendingIfNotExists(12L, "2024/1", "2024-03-01", "2024-03-16", "2024-03-16", false, 0);
        createLendingIfNotExists(6L, "2024/1", "2024-04-01", "2024-04-16", "2024-04-16", true, 20);
        createLendingIfNotExists(1L, "2024/33", "2024-02-01", "2024-02-16", "2024-02-16", false, 0);
        createLendingIfNotExists(9L, "2024/6", "2024-03-01", "2024-03-16", "2024-03-16", false, 0);
        createLendingIfNotExists(3L, "2024/5", "2024-04-01", "2024-04-16", "2024-04-16", false, 0);
        createLendingIfNotExists(2L, "2024/11", "2023-10-18", "2023-11-02", "2023-11-02", false, 0);
        createLendingIfNotExists(9L, "2024/58", "2023-09-18", "2023-10-02", "2023-10-02", true, 20);
        createLendingIfNotExists(10L, "2024/22", "2023-10-18", "2023-11-02", "2023-11-02", false, 0);
        createLendingIfNotExists(5L, "2024/32", "2023-07-14", "2023-07-29", "2023-07-29", true, 5);
        createLendingIfNotExists(12L, "2024/11", "2023-10-17", "2023-11-01", "2023-11-01", false, 0);
        createLendingIfNotExists(11L, "2024/11", "2023-07-18", "2023-08-02", "2023-08-02", true, 15);
        createLendingIfNotExists(7L, "2024/2", "2023-10-18", "2023-11-02", "2023-11-02", true, 20);
        createLendingIfNotExists(17L, "2024/22", "2023-10-18", "2023-11-02", "2023-11-02", false, 0);
        createLendingIfNotExists(17L, "2024/11", "2023-10-18", "2023-11-02", "2023-11-02", false, 0);
        createLendingIfNotExists(6L, "2024/45", "2023-09-18", "2023-10-02", "2023-10-02", false, 0);
        createLendingIfNotExists(18L, "2024/60", "2023-09-18", "2023-10-02", "2023-10-02", false, 0);
        createLendingIfNotExists(15L, "2024/1", "2023-08-18", "2023-09-02", "2023-09-02", true, 15);
        createLendingIfNotExists(13L, "2024/11", "2023-08-18", "2023-09-02", "2023-09-02", true, 20);
        createLendingIfNotExists(4L, "2024/9", "2023-07-18", "2023-08-02", "2023-08-02", true, 40);
        createLendingIfNotExists(2L, "2024/19", "2023-07-18", "2023-08-02", "2023-08-02", false, 0);
        createLendingIfNotExists(14L, "2024/19", "2023-07-18", "2023-07-22", "2023-08-02", false, 0);

        // Criar empréstimos ainda ativos (sem returnDate)
        createActiveLendingIfNotExists(4L, "2024/5", "2024-06-05", "2024-06-20");
        createActiveLendingIfNotExists(5L, "2024/5", "2024-06-07", "2024-06-22");
        createActiveLendingIfNotExists(6L, "2024/6", "2024-05-15", "2024-05-30");
        createActiveLendingIfNotExists(4L, "2024/5", "2024-06-14", "2024-06-29");
        createActiveLendingIfNotExists(5L, "2024/3", "2024-05-10", "2024-05-25");
        createActiveLendingIfNotExists(21L, "2024/12", "2024-05-15", "2024-05-30");
        createActiveLendingIfNotExists(4L, "2024/6", "2024-05-18", "2024-06-02");
        createActiveLendingIfNotExists(17L, "2024/7", "2024-05-10", "2024-05-25");
        createActiveLendingIfNotExists(6L, "2024/8", "2024-05-15", "2024-05-30");
        createActiveLendingIfNotExists(4L, "2024/3", "2024-05-18", "2024-06-02");
        createActiveLendingIfNotExists(5L, "2024/3", "2024-05-10", "2024-05-25");
        createActiveLendingIfNotExists(6L, "2024/12", "2024-05-15", "2024-05-30");
        createActiveLendingIfNotExists(9L, "2024/23", "2024-05-15", "2024-05-30");
        createActiveLendingIfNotExists(6L, "2024/9", "2024-05-15", "2024-05-30");
        createActiveLendingIfNotExists(6L, "2024/17", "2024-05-15", "2024-05-30");
        createActiveLendingIfNotExists(14L, "2024/1", "2024-05-01", "2024-05-16");
        createActiveLendingIfNotExists(15L, "2024/2", "2024-05-05", "2024-05-20");
        createActiveLendingIfNotExists(16L, "2024/59", "2024-05-10", "2024-05-25");
        createActiveLendingIfNotExists(17L, "2024/4", "2024-05-15", "2024-05-30");
        createActiveLendingIfNotExists(18L, "2024/44", "2024-05-20", "2024-06-04");
        createActiveLendingIfNotExists(19L, "2024/6", "2024-05-25", "2024-06-09");
        createActiveLendingIfNotExists(20L, "2024/7", "2024-05-30", "2024-06-14");

        System.out.println("lendingBt.run.exit\n");
    }

    private void createLendingIfNotExists(Long bookID, String readerID, String startDate, String returnDate, String expectedReturnDate, boolean isOverdue, int fine) {
        System.out.println("Checking Book ID: " + bookID + " and Reader ID: " + readerID);

        Optional<Book> book = bookRepo.findById(bookID);
        Optional<Reader> reader = readerRepo.findByReaderID(readerID);

        if (book.isPresent() && reader.isPresent()) {
            if (!lendingExists(bookID, readerID, LocalDate.parse(startDate), LocalDate.parse(returnDate), LocalDate.parse(expectedReturnDate))) {
                Lending lending = new Lending(book.get(), reader.get(), LocalDate.parse(startDate), LocalDate.parse(returnDate), LocalDate.parse(expectedReturnDate), isOverdue, fine);
                lendingRepo.save(lending);
                System.out.println("Lending for Book ID: " + bookID + " and Reader ID: " + readerID + " saved.");
            } else {
                System.out.println("Lending for Book ID: " + bookID + " and Reader ID: " + readerID + " already exists.");
            }
        } else {
            if (!book.isPresent()) {
                System.out.println("Book not found for ID: " + bookID);
            }
            if (!reader.isPresent()) {
                System.out.println("Reader not found for ID: " + readerID);
            }
            System.out.println("Book or Reader not found for Lending creation.");
        }
    }

    private void createActiveLendingIfNotExists(Long bookID, String readerID, String startDate, String expectedReturnDate) {
        System.out.println("Checking Book ID: " + bookID + " and Reader ID: " + readerID);

        Optional<Book> book = bookRepo.findById(bookID);
        Optional<Reader> reader = readerRepo.findByReaderID(readerID);

        if (book.isPresent() && reader.isPresent()) {
            if (!lendingExists(bookID, readerID, LocalDate.parse(startDate), null, LocalDate.parse(expectedReturnDate))) {
                Lending lending = new Lending(book.get(), reader.get(), LocalDate.parse(startDate), null, LocalDate.parse(expectedReturnDate), false, 0);
                lendingRepo.save(lending);
                System.out.println("Active Lending for Book ID: " + bookID + " and Reader ID: " + readerID + " saved.");
            } else {
                System.out.println("Active Lending for Book ID: " + bookID + " and Reader ID: " + readerID + " already exists.");
            }
        } else {
            if (!book.isPresent()) {
                System.out.println("Book not found for ID: " + bookID);
            }
            if (!reader.isPresent()) {
                System.out.println("Reader not found for ID: " + readerID);
            }
            System.out.println("Book or Reader not found for Lending creation.");
        }
    }

    private boolean lendingExists(Long bookID, String readerID, LocalDate startDate, LocalDate returnDate, LocalDate expectedReturnDate) {
        return lendingRepo.findByBook_BookIDAndReader_ReaderIDAndStartDateAndExpectedReturnDate(bookID, readerID, startDate, expectedReturnDate).isPresent();
    }
}
