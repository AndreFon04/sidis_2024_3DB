package org.sidis.book.command.repositories;

import org.sidis.book.command.model.Book;
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

    int findStatusByIsbn(String isbn);

    int findStatusByID(Long bookID);

    Optional<Book> getLastId();

    List<Book> findByAuthorId(String authorID);

    Book save(Book book);
}
