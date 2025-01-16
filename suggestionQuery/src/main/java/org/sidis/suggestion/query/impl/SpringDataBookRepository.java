package org.sidis.suggestion.query.impl;

import org.sidis.suggestion.query.model.BookS;
import org.sidis.suggestion.query.repositories.BookRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface SpringDataBookRepository extends BookRepository, CrudRepository<BookS, Long> {

    @Override
    @Query("SELECT b FROM BookS b ORDER BY b.bookID DESC LIMIT 1")
    Optional<BookS> getLastId();

    @Override
    @Query("SELECT b FROM BookS b")
    List<BookS> findAll();

    @Override
    @Query("SELECT b FROM BookS b WHERE b.isbn LIKE :isbn")
    Optional<BookS> findByIsbn(@Param("isbn") String isbn);

    @Query("SELECT b FROM BookS b WHERE b.bookID = :bookID")
    Optional<BookS> findById(@Param("bookID") Long bookID);

    @Query("SELECT b FROM BookS b JOIN b.genre g WHERE LOWER(g.interest) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<BookS> findByGenre(@Param("genre") String genre);

    @Override
    @Query("SELECT b FROM BookS b WHERE LOWER(b.title) LIKE LOWER(CONCAT(:title, '%'))")
    List<BookS> findByTitle(@Param("title") String title);

    @Query("SELECT b FROM BookS b JOIN b.author a WHERE a.authorID = :authorID")
    List<BookS> findByAuthorId(@Param("authorID") String authorID);
}
