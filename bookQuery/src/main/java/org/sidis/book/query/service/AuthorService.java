package org.sidis.book.query.service;

import org.sidis.book.query.model.Author;

import java.util.List;
import java.util.Optional;


public interface AuthorService {

    List<Author> findByName(String name);

    Optional<Author> findByAuthorID(String authorID);

}
