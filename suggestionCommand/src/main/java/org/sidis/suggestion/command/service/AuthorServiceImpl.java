package org.sidis.suggestion.command.service;

import org.sidis.suggestion.command.model.AuthorS;
import org.sidis.suggestion.command.repositories.AuthorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public List<AuthorS> findByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public Optional<AuthorS> findByAuthorID(String authorID) {
        return authorRepository.findByAuthorID(authorID);
    }

}
