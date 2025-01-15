package org.sidis.suggestion.command.service;

import org.sidis.suggestion.command.exceptions.ConflictException;
import org.sidis.suggestion.command.exceptions.NotFoundException;
import org.sidis.suggestion.command.message_broker.MessagePublisher;
import org.sidis.suggestion.command.model.Suggestion;
import org.sidis.suggestion.command.repositories.BookRepository;
import org.sidis.suggestion.command.repositories.ReaderRepository;
import org.sidis.suggestion.command.repositories.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@Service
public class SuggestionServiceImpl implements SuggestionService {

    private SuggestionRepository repository;
    private BookRepository bookRepository;
    private ReaderRepository readerRepository;
    private RestTemplate restTemplate;
    private MessagePublisher messagePublisher;

    @Autowired
    public SuggestionServiceImpl(SuggestionRepository repository, BookRepository bookRepository, ReaderRepository readerRepository, RestTemplate restTemplate, MessagePublisher messagePublisher) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.readerRepository = readerRepository;
        this.restTemplate = restTemplate;
        this.messagePublisher = messagePublisher;
    }

    @Override
    public List<Suggestion> findAll() {
        return repository.findAll();
    }


    @Override
    public Suggestion create(CreateSuggestionRequest request) {

        if (bookRepository.findByIsbn(request.getIsbn()).isPresent()) {
            throw new ConflictException("Book with ISBN " + request.getIsbn() + " already exists");
        }

        if (readerRepository.findByReaderID(request.getReaderID()).isEmpty()) {
            throw new NotFoundException("Reader with ID " + request.getReaderID() + " not found");
        }

        Suggestion suggestion = new Suggestion(request.getIsbn(), request.getTitle(), request.getAuthorName(), request.getReaderID());
        suggestion.setUniqueSuggestionID();

        System.out.println("About to add suggestion " + suggestion.getSuggestionID() + " " + suggestion.getBookTitle());

        repository.save(suggestion);

        messagePublisher.publishSuggestionCreated(suggestion);

        return suggestion;
    }


    @Override
    public Suggestion partialUpdate(int id, EditSuggestionRequest resource, long desiredVersion) {
        String suggestionID = String.valueOf(id);
        Suggestion suggestion = repository.findBySuggestionID(suggestionID)
                .orElseThrow(() -> new NotFoundException("Suggestion not found."));

        suggestion.setNotes(resource.getNotes());
        Suggestion updatedSuggestion = repository.save(suggestion);

        messagePublisher.publishSuggestionUpdated(updatedSuggestion);

        return updatedSuggestion;
    }
}
