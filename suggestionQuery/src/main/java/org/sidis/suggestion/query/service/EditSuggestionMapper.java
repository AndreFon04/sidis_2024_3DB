package org.sidis.suggestion.query.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.sidis.suggestion.query.model.Suggestion;


@Mapper(componentModel = "spring")
public abstract class EditSuggestionMapper {

    @Mapping(source = "isbn", target = "bookISBN")
    @Mapping(source = "title", target = "bookTitle")
    @Mapping(source = "authorName", target = "bookAuthorName")
    public abstract Suggestion create(CreateSuggestionRequest request, @MappingTarget Suggestion suggestion);


//    @Mapping(source = "notes", target = "notes")
//    @Mapping(target = "suggestionID", ignore = true) // Ignora o ID pois este não muda
//    @Mapping(target = "version", ignore = true) // Ignora a versão pois ela será tratada automaticamente
//    @Mapping(target = "isbn", ignore = true) // Ignora o ID do livro, pois ele não será atualizado neste caso
//    @Mapping(target = "readerID", ignore = true) // Ignora o ID do leitor
    public abstract void update(EditSuggestionRequest request, @MappingTarget Suggestion suggestion);

//    // Método para obter uma suggestion pelo ID
//    public Suggestion toSuggestion(final String suggestionID) {
//        return repository.findBySuggestionID(suggestionID)
//                .orElseThrow(() -> new ValidationException("Select an existing suggestion"));
//    }
}
