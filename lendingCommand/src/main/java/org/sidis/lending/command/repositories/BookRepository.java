package org.sidis.lending.command.repositories;

import org.sidis.lending.command.dto.AuthorDTO;
import org.sidis.lending.command.dto.BookDTO;
import org.sidis.lending.command.model.BookL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository {

    List<BookL> findByGenre(String genre);

    List<BookL> findByTitle(String title);

    List<BookL> findAll();

    Optional<BookL> findByIsbn(String isbn);

    Optional<BookL> findBookByBookID(Long bookID);

    Optional<BookL> getLastId();

    List<BookL> findByAuthorId(String authorID);

    <S extends BookL> S save(S entity);
}
