package org.sidis.lending.command.service;

import org.sidis.lending.command.dto.AuthorDTO;
import org.sidis.lending.command.model.AuthorL;
import org.sidis.lending.command.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorL> findByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public Optional<AuthorL> findByAuthorID(String authorID) {
        return authorRepository.findByAuthorID(authorID);
    }

}
