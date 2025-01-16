package org.sidis.suggestion.query.repositories;

import org.sidis.suggestion.query.model.AuthorS;

import java.util.List;
import java.util.Optional;


public interface AuthorRepository {

    List<AuthorS> findByName(String name);

    Optional<AuthorS> findByAuthorID(String authorID);

    List<AuthorS> findByNameAndBiography(String name, String authorBiography);

    <S extends AuthorS> S save(S entity);

    Optional<AuthorS> findTopByOrderByAuthorIDDesc();
}
