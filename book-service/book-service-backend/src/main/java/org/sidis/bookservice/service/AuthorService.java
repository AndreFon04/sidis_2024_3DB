package org.sidis.bookservice.service;

import org.sidis.bookservice.model.Author;
import org.sidis.bookservice.model.CoAuthorDTO;

import java.util.List;
import java.util.Optional;


public interface AuthorService {

    List<Author> findByName(String name);

    Optional<Author> findByAuthorID(String authorID);

    Author create(CreateAuthorRequest request);

    Author partialUpdate(String authorID, EditAuthorRequest request, long parseLong);

    List<CoAuthorDTO> getCoAuthorsAndBooks(String authorId);
}
