package org.sidis.suggestion.command.service;

import org.sidis.suggestion.command.model.BookS;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface BookService {

    Optional<BookS> getBookById(final Long bookID);

    Optional<BookS> getBookByIsbn(final String isbn);

    List<BookS> getAll();

    List<BookS> getBookByGenre(final String genre);

    List<BookS> getBookByTitle(final String title);



    List<BookS> getBooksByAuthorId(String authorID);
}
