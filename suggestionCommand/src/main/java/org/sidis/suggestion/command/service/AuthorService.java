package org.sidis.suggestion.command.service;

import org.sidis.suggestion.command.model.AuthorS;

import java.util.List;
import java.util.Optional;


public interface AuthorService {

    List<AuthorS> findByName(String name);

    Optional<AuthorS> findByAuthorID(String authorID);
}
