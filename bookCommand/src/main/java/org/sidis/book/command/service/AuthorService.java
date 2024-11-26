package org.sidis.book.command.service;

import org.sidis.book.command.model.Author;
import org.sidis.book.command.model.CoAuthorDTO;

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
