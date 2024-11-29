package org.sidis.book.query.service;

import org.sidis.book.query.model.Author;
import org.sidis.book.query.model.CoAuthorDTO;
import org.sidis.book.query.service.EditAuthorRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface AuthorService {

    List<Author> findByName(String name);

    Optional<Author> findByAuthorID(String authorID);

    Author create(CreateAuthorRequest request, UUID authorID);

    Author partialUpdate(String authorID, EditAuthorRequest request, long parseLong);

    List<CoAuthorDTO> getCoAuthorsAndBooks(String authorId);
}
