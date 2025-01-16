package org.sidis.suggestion.query.service;

import org.sidis.suggestion.query.model.AuthorS;

import java.util.List;
import java.util.Optional;


public interface AuthorService {

    List<AuthorS> findByName(String name);

    Optional<AuthorS> findByAuthorID(String authorID);
}
