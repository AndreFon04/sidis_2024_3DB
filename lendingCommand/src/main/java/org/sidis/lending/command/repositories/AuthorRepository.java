package org.sidis.lending.command.repositories;

import org.sidis.lending.command.model.AuthorL;

import java.util.List;
import java.util.Optional;


public interface AuthorRepository {

    List<AuthorL> findByName(String name);

    Optional<AuthorL> findByAuthorID(String authorID);

    List<AuthorL> findByNameAndBiography(String name, String authorBiography);

    <S extends AuthorL> S save(S entity);

    Optional<AuthorL> findTopByOrderByAuthorIDDesc();
}
