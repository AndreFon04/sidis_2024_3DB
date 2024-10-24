package org.sidis.booksauthors.bookmanagement.services;

import com.example.library.bookmanagement.model.Book;
import com.example.library.bookmanagement.model.Genre;
import com.example.library.bookmanagement.repositories.BookRepository;
import com.example.library.bookmanagement.repositories.GenreRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
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
