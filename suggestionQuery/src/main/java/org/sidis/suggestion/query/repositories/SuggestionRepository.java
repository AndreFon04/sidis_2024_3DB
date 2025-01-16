package org.sidis.suggestion.query.repositories;

import org.sidis.suggestion.query.model.Suggestion;

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
