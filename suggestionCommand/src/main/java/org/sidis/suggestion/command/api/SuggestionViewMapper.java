package org.sidis.suggestion.command.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sidis.suggestion.command.model.Suggestion;

import java.util.List;


@Mapper(componentModel = "spring")
public interface SuggestionViewMapper {

    SuggestionView toSuggestionView(Suggestion suggestion);

    Iterable<SuggestionView> toSuggestionView(Iterable<Suggestion> suggestions);

    List<SuggestionView> toSuggestionView(List<Suggestion> suggestions);

}
