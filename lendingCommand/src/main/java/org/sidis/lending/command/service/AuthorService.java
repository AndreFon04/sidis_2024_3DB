package org.sidis.lending.command.service;

import org.sidis.lending.command.model.AuthorL;

import java.util.List;
import java.util.Optional;


public interface AuthorService {

    List<AuthorL> findByName(String name);

    Optional<AuthorL> findByAuthorID(String authorID);
}
