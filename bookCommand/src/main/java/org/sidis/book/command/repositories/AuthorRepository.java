package org.sidis.book.command.repositories;

import org.sidis.book.command.model.Author;
import org.sidis.book.command.model.Book;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorRepository {

    List<Author> findByName(String name);

    Optional<Author> findByAuthorID(String authorID);

    List<Author> findByNameAndBiography(String name, String authorBiography);

    <S extends Author> S save(S entity);

    Optional<Author> findTopByOrderByAuthorIDDesc();

    List<Book> findByAuthorsContaining(Author author);
}
