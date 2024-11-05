package org.sidis.bookservice.repositories;

import org.sidis.bookservice.model.Author;
import org.sidis.bookservice.model.Book;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    List<Author> findByName(String name);

    Optional<Author> findByAuthorID(String authorID);

    List<Author> findByNameAndBiography(String name, String authorBiography);

    <S extends Author> S save(S entity);

    Optional<Author> findTopByOrderByAuthorIDDesc();

    List<Book> findByAuthorsContaining(Author author);
}
