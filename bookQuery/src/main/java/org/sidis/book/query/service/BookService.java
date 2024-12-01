package org.sidis.book.query.service;

import org.sidis.book.query.model.Book;
import org.sidis.book.query.model.BookCountDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Service
public interface BookService {

    Optional<Book> getBookByIsbn(final String isbn);

    List<Book> getAll();

    List<Book> getBookByGenre(final String genre);

    List<Book> getBookByTitle(final String title);

    List<Map.Entry<String, Long>> findTop5Genres();

    List<BookCountDTO> findTop5Books();

    Optional<Book> getBookById(final Long bookID);

    List<Book> getBooksByAuthorId(String authorID);
}
