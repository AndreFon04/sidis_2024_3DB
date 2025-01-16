package org.sidis.suggestion.query.service;

import org.sidis.suggestion.query.exceptions.ConflictException;
import org.sidis.suggestion.query.exceptions.NotFoundException;
import org.sidis.suggestion.query.model.Suggestion;
import org.sidis.suggestion.query.repositories.BookRepository;
import org.sidis.suggestion.query.repositories.ReaderRepository;
import org.sidis.suggestion.query.repositories.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;


@Service
public class SuggestionServiceImpl implements SuggestionService {

    private SuggestionRepository repository;

    @Autowired
    public SuggestionServiceImpl(SuggestionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Suggestion> findAll() {
        return repository.findAll();
    }

    @Override
    public Suggestion saveSuggestion(Suggestion suggestion) {
        repository.save(suggestion);
        return suggestion;
    }

    @Override
    public Optional<Suggestion> getSuggestionByID(final String suggestionID) {
        return repository.findBySuggestionID(suggestionID);
    }

    @Override
    public List<Suggestion> getSuggestionByISBN(final String isbn) {
        return repository.findByIsbn(isbn);
    }

    @Override
    public List<Suggestion> getSuggestionByReaderID(final String readerID) {
        return repository.findByReaderID(readerID);
    }

}
