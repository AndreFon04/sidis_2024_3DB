package org.sidis.user.query.service;

import jakarta.validation.ValidationException;
import org.sidis.user.query.client.BookServiceClient;
import org.sidis.user.query.client.LendingServiceClient;
import org.sidis.user.query.client.ReaderServiceClient;
import org.sidis.user.query.exceptions.ConflictException;
import org.sidis.user.query.exceptions.NotFoundException;
import org.sidis.user.query.model.Reader;
import org.sidis.user.query.model.ReaderCountDTO;
import org.sidis.user.query.model.User;
import org.sidis.user.query.repositories.ReaderRepository;
import org.sidis.user.query.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;
    private final UserRepository userRepository;
    private final EditReaderMapper editReaderMapper;
    private final EditUserMapper editUserMapper;
    private final PasswordEncoder passwordEncoder;
    private final LendingServiceClient lendingServiceClient;
    private final BookServiceClient bookServiceClient;
    private final ReaderServiceClient readerServiceClient;


    public ReaderServiceImpl(ReaderRepository readerRepository, UserRepository userRepository, EditReaderMapper editReaderMapper, EditUserMapper editUserMapper,
                             PasswordEncoder passwordEncoder, LendingServiceClient lendingServiceClient, BookServiceClient bookServiceClient, ReaderServiceClient readerServiceClient) {
        this.readerRepository = readerRepository;
        this.userRepository = userRepository;
        this.editReaderMapper = editReaderMapper;
        this.editUserMapper = editUserMapper;
        this.passwordEncoder = passwordEncoder;
        this.lendingServiceClient = lendingServiceClient;
        this.bookServiceClient = bookServiceClient;
        this.readerServiceClient = readerServiceClient;
    }

    @Override
    public List<Reader> findAll() {
        return readerRepository.findAll();
    }


    @Override
    public Reader saveReader(Reader reader) {
        readerRepository.save(reader);
        return reader;
    }

    @Override
    public Optional<Reader> getReaderByID(final String readerID) {
        return readerRepository.findByReaderID(readerID);
    }

    @Override
    public Optional<Reader> getReaderByEmail(final String email) {
        return readerRepository.findByEmail(email);
    }

    @Override
    public List<Reader> getReaderByName(final String name) {
        return readerRepository.findByName(name);
    }

//    public List<ReaderCountDTO> findTop5Readers() {
//        // Obter todos os lendings
//        List<LendingDTO> lendings = lendingServiceClient.getAllLendings();
//
//        // Contar quantos empréstimos cada reader fez
//        Map<String, Long> readerIdCounts = lendings.stream()
//                .collect(Collectors.groupingBy(LendingDTO::getReaderID, Collectors.counting()));
//
//        // Obter os 5 readers com mais empréstimos
//        List<Map.Entry<String, Long>> top5Readers = readerIdCounts.entrySet().stream()
//                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
//                .limit(5)
//                .collect(Collectors.toList());
//
//
//        return top5Readers.stream()
//                .map(entry -> new ReaderCountDTO(entry.getKey(), entry.getValue()))
//                .collect(Collectors.toList());
//    }
//
//    public List<GenreDTO> getBookSuggestions(Reader reader) {
//        Set<String> interests = getInterestsByReader(reader);
//        List<GenreDTO> suggestions = new ArrayList<>();
//
//        for (String interest : interests) {
//            suggestions.addAll(bookServiceClient.getBooksByGenre(interest));
//        }
//
//        return suggestions;
//    }

//    public Set<String> getInterestsByReader(Reader reader) {
//        return reader.getInterests();
//    }
}
