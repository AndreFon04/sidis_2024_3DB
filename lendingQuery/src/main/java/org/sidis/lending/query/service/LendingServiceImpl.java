package org.sidis.lending.query.service;

import org.sidis.lending.query.exceptions.NotFoundException;
import org.sidis.lending.query.model.Lending;
import org.sidis.lending.query.repositories.LendingRepository;
import org.sidis.lending.query.service.CreateLendingRequest;
import org.sidis.lending.query.service.EditLendingRequest;
import org.sidis.lending.query.service.ExternalServiceHelper;
import org.sidis.lending.query.service.LendingService;
import org.springframework.beans.factory.annotation.Autowired;
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

//    @Value("${server.port}")
//    private String currentPort; // Porta da instância atual
//
//    @Value("${lending.instance1.url}")
//    private String lendingInstance1Url;
//
//    @Value("${lending.instance2.url}")
//    private String lendingInstance2Url;

    @Autowired
    public LendingServiceImpl(LendingRepository lendingRepository, RestTemplate restTemplate, ExternalServiceHelper externalServiceHelper) {
        this.lendingRepository = lendingRepository;
        this.restTemplate = restTemplate;
        this.externalServiceHelper = externalServiceHelper;
        System.out.println("LendingServiceImpl created with ExternalServiceHelper: " + (externalServiceHelper != null));
    }

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
