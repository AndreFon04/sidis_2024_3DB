package org.sidis.suggestion.command.service;

import org.sidis.suggestion.command.model.ReaderS;
import org.sidis.suggestion.command.model.Suggestion;

import java.util.List;


public interface SuggestionService {

    List<Suggestion> findAll();

    Suggestion create(CreateSuggestionRequest request);

    Suggestion partialUpdate(int id, EditSuggestionRequest resource, long desiredVersion);

}
