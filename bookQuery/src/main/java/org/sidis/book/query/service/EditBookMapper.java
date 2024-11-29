package org.sidis.book.query.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.sidis.book.query.model.Book;
import org.sidis.book.query.model.Genre;
import org.sidis.book.query.repositories.BookRepository;
import org.sidis.book.query.repositories.GenreRepository;
import org.sidis.book.query.service.CreateBookRequest;
import org.sidis.book.query.service.EditBookRequest;
import org.springframework.beans.factory.annotation.Autowired;


@Mapper(componentModel = "spring")
public abstract class EditBookMapper {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GenreRepository genreRepository;

    public abstract Book create(CreateBookRequest request);

    @Mapping(target = "bookID", ignore = true)
    @Mapping(target = "isbn", ignore = true)
    @Mapping(target = "genre", source = "genre")
    public abstract void update(EditBookRequest request, @MappingTarget Book book);

    protected Genre map(String genre) {
        return genreRepository.findByInterest(genre);
    }
}
