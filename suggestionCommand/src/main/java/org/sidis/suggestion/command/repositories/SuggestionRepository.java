package org.sidis.suggestion.command.repositories;

import org.sidis.suggestion.command.model.BookS;
import org.sidis.suggestion.command.model.ReaderS;
import org.sidis.suggestion.command.model.Suggestion;

import java.util.List;
import java.util.Optional;


public interface SuggestionRepository {

    Optional<Suggestion> findTopByOrderBySuggestionIDDesc();

    Optional<Suggestion> findBySuggestionID(String suggestionID);

    List<Suggestion> findByIsbn(String isbn);

    List<Suggestion> findByReaderID(String readerID);

    List<Suggestion> findAll();

    <S extends Suggestion> S save(S entity);
}
