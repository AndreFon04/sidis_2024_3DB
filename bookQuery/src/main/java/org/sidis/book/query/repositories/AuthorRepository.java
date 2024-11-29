package org.sidis.book.query.repositories;

import org.sidis.book.query.model.Author;
import org.sidis.book.query.model.Book;

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
