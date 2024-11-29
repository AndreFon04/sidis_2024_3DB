package org.sidis.book.query.repositories;

import org.sidis.book.query.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository {

    List<Book> findByGenre(String genre);

    List<Book> findByTitle(String title);

    List<Book> findAll();

    Optional<Book> findByIsbn(String isbn);

    Optional<Book> findBookByBookID(Long bookID);

    Optional<Book> getLastId();

    List<Book> findByAuthorId(String authorID);

    Book save(Book book);
}
