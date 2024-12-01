package org.sidis.book.command.service;

import org.sidis.book.command.model.Author;


public interface AuthorService {

    Author create(CreateAuthorRequest request);

    Author partialUpdate(String authorID, EditAuthorRequest request, long parseLong);
}
