package org.sidis.book.command.service;

import org.sidis.book.command.model.Book;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public interface BookService {

    Book create(CreateBookRequest request);

    Optional<Book> getBookById(final Long bookID);

    Book partialUpdate(Long bookID, EditBookRequest request, long desiredVersion);
}
