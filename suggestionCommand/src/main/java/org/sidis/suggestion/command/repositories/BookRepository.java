package org.sidis.suggestion.command.repositories;

import org.sidis.suggestion.command.model.BookS;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository {

    List<BookS> findByGenre(String genre);

    List<BookS> findByTitle(String title);

    List<BookS> findAll();

    Optional<BookS> findByIsbn(String isbn);

    Optional<BookS> findBookByBookID(Long bookID);

    Optional<BookS> getLastId();

    List<BookS> findByAuthorId(String authorID);

    <S extends BookS> S save(S entity);
}
