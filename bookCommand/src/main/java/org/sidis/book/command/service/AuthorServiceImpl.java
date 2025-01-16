package org.sidis.book.command.service;

import org.sidis.book.command.exceptions.NotFoundException;
import org.sidis.book.command.message_broker.MessagePublisher;
import org.sidis.book.command.model.Author;
import org.sidis.book.command.repositories.AuthorRepository;
import org.springframework.stereotype.Service;



@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final MessagePublisher sender;


    public AuthorServiceImpl(AuthorRepository authorRepository, MessagePublisher sender) {
        this.authorRepository = authorRepository;
        this.sender = sender;
    }

    @Override
    public Author create(CreateAuthorRequest request) {
        if (request.getBiography() == null || request.getBiography().length() > 4096) {
            throw new IllegalArgumentException("The biography cannot be null, nor have more than 4096 characters.");
        }
        if (request.getName() == null || request.getName().length() > 150) {
            throw new IllegalArgumentException("The name cannot be null, nor have more than 150 characters.");
        }

        final Author author = new Author(request.getName(), request.getBiography());
        author.setUniqueAuthorID();

        authorRepository.save(author);

        sender.publishAuthorCreated(author);

        return author;
    }


    @Override
    public Author partialUpdate(final String authorID, final EditAuthorRequest request, final long desiredVersion) {
        final var author = authorRepository.findByAuthorID(authorID)
                .orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));

        author.applyPatch(desiredVersion, request.getName(), request.getBiography());

        authorRepository.save(author);

        sender.publishAuthorUpdated(author);

        return author;
    }
}
