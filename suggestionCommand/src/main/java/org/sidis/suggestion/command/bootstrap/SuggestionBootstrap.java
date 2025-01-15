package org.sidis.suggestion.command.bootstrap;

import org.sidis.suggestion.command.model.ReaderS;
import org.sidis.suggestion.command.model.Suggestion;
import org.sidis.suggestion.command.repositories.SuggestionRepository;
import org.sidis.suggestion.command.service.SuggestionService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Component
//@Profile("bootstrap")
public class SuggestionBootstrap implements CommandLineRunner {

    private final SuggestionRepository repository;

    public SuggestionBootstrap(SuggestionRepository repository) {
        this.repository = repository;
    }

    @Override
    @Transactional
    public void run(final String... args) throws Exception {
        System.out.println("SuggestionBootstrapper running...");

        final Optional<Suggestion> s = repository.findTopByOrderBySuggestionIDDesc();
        if (s.isPresent()) {
            System.out.println("Last suggestionID is " + s.get().getSuggestionID());
            s.get().initCounter(s.get().getSuggestionID());
        }

        if (repository.findByIsbn("123456789").isEmpty()) {
            System.out.println("sgBt.s1");
            final Suggestion s1 = new Suggestion("123456789", "QLQR COISA", "QLQR ALGUEM", "2025/1");
            repository.save(s1);
            System.out.println("sgBt.s1.save");
        }
        addSuggestionIndividually();
        System.out.println("SuggestionBootstrapper finished.");
    }

    private void addSuggestionIndividually() {
        addSuggestion("123456788", "Segunda qlqr coisa", "Segunda qlqr alguem", "2023/2");
    }

    private void addSuggestion(final String bookISBN, final String bookTitle, final String bookAuthorName, final String readerID) {
        if (repository.findByIsbn(bookISBN).isEmpty()) {
            Suggestion suggestion = new Suggestion(bookISBN, bookTitle, bookAuthorName, readerID);
            repository.save(suggestion);
        }
    }
}
