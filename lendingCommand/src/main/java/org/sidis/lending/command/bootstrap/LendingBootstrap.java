package org.sidis.lending.command.bootstrap;

import org.sidis.lending.command.model.Lending;
import org.sidis.lending.command.repositories.LendingRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;


@Component
//@Profile("bootstrap")
public class LendingBootstrap implements CommandLineRunner {

    private final LendingRepository lendingRepo;

    public LendingBootstrap(@Qualifier("springDataLendingRepository") LendingRepository lendingRepo) {
        this.lendingRepo = lendingRepo;
    }

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        System.out.println("LendingBootstrapper running...");
        System.out.println("LendingBootstrapper running with profile: " + System.getProperty("spring.profiles.active"));

        // Verificar o último empréstimo e inicializar o contador se necessário
        Optional<Lending> lastLending = lendingRepo.findFirstByOrderByLendingIDDesc();
        if (lastLending.isPresent()) {
            lastLending.get().initCounter(lastLending.get().getLendingID());
            System.out.println("Lending counter initialized with last ID: " + lastLending.get().getLendingID());
        } else {
            System.out.println("No previous lending found, counter initialization skipped.");
        }

        // Exemplo de empréstimos devolvidos a tempo
        createLendingIfNotExists(1L, "2024/1", "2024-01-01", "2024-01-14", "2024-01-16", false, 0);
        createLendingIfNotExists(2L, "2024/2", "2024-03-01", "2024-03-14", "2024-03-16", false, 0);
        createLendingIfNotExists(3L, "2024/3", "2024-05-10", "2024-05-24", "2024-05-25", false, 0);

        // Exemplo de empréstimos devolvidos em atraso
        createLendingIfNotExists(4L, "2024/4", "2024-02-01", "2024-02-19", "2024-02-16", true, 10);
        createLendingIfNotExists(5L, "2024/5", "2024-04-10", "2024-04-30", "2024-04-25", true, 20);
        createLendingIfNotExists(6L, "2024/6", "2024-06-01", "2024-06-18", "2024-06-16", true, 15);

        // Exemplo de empréstimos pendentes (ainda não devolvidos)
        createActiveLendingIfNotExists(7L, "2024/7", "2024-07-01", "2024-07-16");
        createActiveLendingIfNotExists(8L, "2024/8", "2024-08-01", "2024-08-16");
        createActiveLendingIfNotExists(9L, "2024/9", "2024-09-15", "2024-09-30");

        // Empréstimo em atraso (não devolvido)
        createActiveLendingIfNotExists(10L, "2024/10", "2024-06-01", "2024-06-16"); // Pendente, em atraso
        createActiveLendingIfNotExists(11L, "2024/11", "2024-05-15", "2024-05-30"); // Pendente, em atraso

        // Exemplo de empréstimos mais espalhados pelos meses (de Janeiro até Setembro)
        createLendingIfNotExists(12L, "2024/12", "2024-02-01", "2024-02-16", "2024-02-18", false, 0); // Devolvido a tempo
        createLendingIfNotExists(13L, "2024/13", "2024-03-01", "2024-03-18", "2024-03-20", true, 5);  // Devolvido em atraso
        createLendingIfNotExists(14L, "2024/14", "2024-04-01", "2024-04-15", "2024-04-16", false, 0); // Devolvido a tempo
        createLendingIfNotExists(15L, "2024/15", "2024-08-01", null, "2024-08-16", false, 0);         // Não devolvido ainda

        System.out.println("LendingBootstrapper finished.");
    }

    private void createLendingIfNotExists(Long bookID, String readerID, String startDate, String returnDate, String expectedReturnDate, boolean isOverdue, int fine) {
        if (!lendingExists(bookID, readerID, LocalDate.parse(startDate), LocalDate.parse(expectedReturnDate))) {
            Lending lending = new Lending(bookID, readerID, LocalDate.parse(startDate), returnDate != null ? LocalDate.parse(returnDate) : null, LocalDate.parse(expectedReturnDate), isOverdue, fine);
            lendingRepo.save(lending);
            System.out.println("Lending created for Book ID: " + bookID + " and Reader ID: " + readerID);
        } else {
            System.out.println("Lending already exists for Book ID: " + bookID + " and Reader ID: " + readerID);
        }
    }

    private void createActiveLendingIfNotExists(Long bookID, String readerID, String startDate, String expectedReturnDate) {
        if (!lendingExists(bookID, readerID, LocalDate.parse(startDate), LocalDate.parse(expectedReturnDate))) {
            Lending lending = new Lending(bookID, readerID, LocalDate.parse(startDate), null, LocalDate.parse(expectedReturnDate), false, 0);
            lendingRepo.save(lending);
            System.out.println("Active Lending created for Book ID: " + bookID + " and Reader ID: " + readerID);
        } else {
            System.out.println("Active Lending already exists for Book ID: " + bookID + " and Reader ID: " + readerID);
        }
    }

    private boolean lendingExists(Long bookID, String readerID, LocalDate startDate, LocalDate expectedReturnDate) {
        boolean exists = lendingRepo.findByBookIDAndReaderIDAndStartDateAndExpectedReturnDate(bookID, readerID, startDate, expectedReturnDate).isPresent();
        System.out.println("Lending exists: " + exists + " for Book ID: " + bookID + " and Reader ID: " + readerID);
        return exists;
    }
}
