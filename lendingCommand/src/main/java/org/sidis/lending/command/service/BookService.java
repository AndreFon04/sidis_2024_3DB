package org.sidis.lending.command.service;

import org.sidis.lending.command.model.BookL;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface BookService {

    Optional<BookL> getBookByIsbn(final String isbn);

    List<BookL> getAll();

    List<BookL> getBookByGenre(final String genre);

    List<BookL> getBookByTitle(final String title);

    Optional<BookL> getBookById(final Long bookID);

    List<BookL> getBooksByAuthorId(String authorID);
}
