package org.sidis.lending.command.service;

import org.sidis.lending.command.exceptions.NotFoundException;
import org.sidis.lending.command.message_broker.MessagePublisher;
import org.sidis.lending.command.model.Lending;
import org.sidis.lending.command.repositories.LendingRepository;
import org.sidis.lending.command.service.EditLendingRequest;
import org.sidis.lending.command.service.ExternalServiceHelper;
import org.sidis.lending.command.service.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public class LendingServiceImpl implements LendingService {

    private LendingRepository lendingRepository;
    private RestTemplate restTemplate;
    private ExternalServiceHelper externalServiceHelper;
    private MessagePublisher messagePublisher;

//    @Value("${server.port}")
//    private String currentPort; // Porta da instância atual
//
//    @Value("${lending.instance1.url}")
//    private String lendingInstance1Url;
//
//    @Value("${lending.instance2.url}")
//    private String lendingInstance2Url;

    @Autowired
    public LendingServiceImpl(LendingRepository lendingRepository, RestTemplate restTemplate, ExternalServiceHelper externalServiceHelper, MessagePublisher messagePublisher) {
        this.lendingRepository = lendingRepository;
        this.restTemplate = restTemplate;
        this.externalServiceHelper = externalServiceHelper;
        this.messagePublisher = messagePublisher;
        System.out.println("LendingServiceImpl created with ExternalServiceHelper: " + (externalServiceHelper != null));
    }

    @Override
    public Optional<String> findReaderByLendingID(String lendingID) {
        // Corrigido para retornar apenas o ID do leitor
        return lendingRepository.findReaderByLendingID(lendingID);
    }

    @Override
    public Optional<Lending> getLastId() {
        return lendingRepository.findFirstByOrderByLendingIDDesc();
    }

    public List<Lending> getOverdueLendingsSortedByTardiness() {
        return lendingRepository.findByOverdueTrueOrderByTardinessDesc();
    }

    @Override
    public Lending create(CreateLendingRequest request) {
        String isbn = messagePublisher.askBookISBNbyID(request.getBookID());

        if (isbn == null) {
            throw new NotFoundException("Book not found");
        }

        boolean validReaderID = messagePublisher.askReaderValidbyReaderID(request.getReaderID());

        if (!validReaderID) {
            throw new NotFoundException("Reader ID not found");
        }

        boolean hasOverdueLending = lendingRepository.existsByReaderIDAndOverdueTrue(request.getReaderID());
        if (hasOverdueLending) {
            throw new IllegalArgumentException("Reader has overdue lending and cannot borrow more books.");
        }

        long activeLendingsCount = lendingRepository.countActiveLendingsByReaderID(request.getReaderID());
        if (activeLendingsCount >= 3) {
            throw new IllegalArgumentException("Reader already has the maximum number of active lendings (3).");
        }

        LocalDate startDate = LocalDate.now();
        LocalDate expectedReturnDate = startDate.plusDays(14); // Exemplo de prazo de devolução

        Lending lending = new Lending(request.getBookID(), request.getReaderID(), startDate, null, expectedReturnDate, false, 0);
        lending.updateOverdueStatus();

        lendingRepository.save(lending);

        messagePublisher.publishLendingCreated(lending);

        return lending;
    }


    @Override
    public Lending partialUpdate(int id1, int id2, EditLendingRequest resource, long desiredVersion) {
        String lendingID = id1 + "/" + id2;
        Lending lending = lendingRepository.findByLendingID(lendingID)
                .orElseThrow(() -> new NotFoundException("Lending not found."));
        if (resource.getReturnDate() != null) {
            lending.setReturnDate(resource.getReturnDate());
            lending.updateOverdueStatus();
            lending.setFine(calculateFine(lending));
        }

        lending.setNotes(resource.getNotes());
        Lending updatedLending = lendingRepository.save(lending);

        // Sincronizar com a outra instância via HTTP POST
//        String otherInstanceUrl = getOtherInstanceUrl();
//        try {
//            restTemplate.postForEntity(otherInstanceUrl + "/api/lendings/sync", updatedLending, Lending.class);
//        } catch (Exception e) {
//            System.err.println("Erro ao sincronizar o lending atualizado com a outra instância: " + e.getMessage());
//        }
        return updatedLending;
    }

    @Override
    public int calculateFine(String lendingID) {
        Optional<Lending> lending = lendingRepository.findByLendingID(lendingID);
        return lending.map(this::calculateFine).orElse(0);
    }

    private int calculateFine(Lending lending) {
        LocalDate expectedReturnDate = lending.getExpectedReturnDate();
        LocalDate returnDate = lending.getReturnDate();
        if (returnDate != null && returnDate.isAfter(expectedReturnDate)) {
            long daysLate = ChronoUnit.DAYS.between(expectedReturnDate, returnDate);
            return (int) (daysLate * 5); // Exemplo de multa de 5 por dia de atraso
        }
        return 0;
    }

    @Override
    public double getAverageLendingDuration() {
        List<Lending> lendings = lendingRepository.findLendingsWithReturnDate();
        if (lendings.isEmpty()) {return 0;}
        long totalDays = lendings.stream()
                .mapToLong(l -> ChronoUnit.DAYS.between(l.getStartDate(), l.getReturnDate()))
                .sum();
        return (double) totalDays / lendings.size();
    }

    @Override
    public Map<String, Double> getAverageLendingsPerGenre(int month, int year) {
        Map<String, Double> genreLendingAverage = new HashMap<>();
        Map<String, Integer> genreCount = new HashMap<>();
        List<Object[]> lendings = lendingRepository.findLendingsDurationByBookAndMonth(month, year);
        int totalLendings = lendings.size();

        for (Object[] lending : lendings) {
            Long bookID = (Long) lending[0];
            String genre = externalServiceHelper.getBookGenreFromService(bookID);

            genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1);
        }
        for (String genre : genreCount.keySet()) {
            int count = genreCount.get(genre);
            genreLendingAverage.put(genre, (double) count / totalLendings); // Média de empréstimos para o género
        }
        return genreLendingAverage;
    }
}
