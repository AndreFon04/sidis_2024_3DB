package org.sidis.book.query.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.sidis.book.query.model.Author;
import org.sidis.book.query.service.EditAuthorRequest;

@Mapper(componentModel = "spring")
public abstract class EditAuthorMapper {

    public abstract Author create(CreateAuthorRequest request);

    @Mapping(target = "authorID", ignore = true)
    public abstract void  update(EditAuthorRequest request, @MappingTarget Author author);

}
