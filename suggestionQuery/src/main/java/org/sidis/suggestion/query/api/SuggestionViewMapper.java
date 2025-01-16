package org.sidis.suggestion.query.api;

import org.mapstruct.Mapper;
import org.sidis.suggestion.query.api.SuggestionView;
import org.sidis.suggestion.query.model.Suggestion;

import java.util.List;


@Mapper(componentModel = "spring")
public interface SuggestionViewMapper {

    SuggestionView toSuggestionView(Suggestion suggestion);

    Iterable<SuggestionView> toSuggestionView(Iterable<Suggestion> suggestions);

    List<SuggestionView> toSuggestionView(List<Suggestion> suggestions);

}
