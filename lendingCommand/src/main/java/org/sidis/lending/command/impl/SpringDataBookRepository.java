package org.sidis.lending.command.impl;

import org.sidis.lending.command.dto.BookDTO;
import org.sidis.lending.command.model.BookL;
import org.sidis.lending.command.repositories.BookRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface SpringDataBookRepository extends BookRepository, CrudRepository<BookL, Long> {

    @Override
    @Query("SELECT b FROM BookL b ORDER BY b.bookID DESC LIMIT 1")
    Optional<BookL> getLastId();

    @Override
    @Query("SELECT b FROM BookL b")
    List<BookL> findAll();

    @Override
    @Query("SELECT b FROM BookL b WHERE b.isbn LIKE :isbn")
    Optional<BookL> findByIsbn(@Param("isbn") String isbn);

    @Query("SELECT b FROM BookL b WHERE b.bookID = :bookID")
    Optional<BookL> findById(@Param("bookID") Long bookID);

    @Query("SELECT b FROM BookL b JOIN b.genre g WHERE LOWER(g.interest) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<BookL> findByGenre(@Param("genre") String genre);

    @Override
    @Query("SELECT b FROM BookL b WHERE LOWER(b.title) LIKE LOWER(CONCAT(:title, '%'))")
    List<BookL> findByTitle(@Param("title") String title);

    @Query("SELECT b FROM BookL b JOIN b.author a WHERE a.authorID = :authorID")
    List<BookL> findByAuthorId(@Param("authorID") String authorID);
}
