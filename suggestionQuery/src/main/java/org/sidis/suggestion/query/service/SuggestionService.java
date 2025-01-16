package org.sidis.suggestion.query.service;

import org.sidis.suggestion.query.model.Suggestion;
import org.sidis.suggestion.query.service.EditSuggestionRequest;

import java.util.List;
import java.util.Optional;


public interface SuggestionService {

    List<Suggestion> findAll();

    Suggestion saveSuggestion(Suggestion suggestion);

    Optional<Suggestion> getSuggestionByID(final String suggestionID);

    List<Suggestion> getSuggestionByISBN(final String isbn);

    List<Suggestion> getSuggestionByReaderID(final String readerID);
}
