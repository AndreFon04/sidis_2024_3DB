package org.sidis.lending.command.service;

import org.sidis.lending.command.exceptions.NotFoundException;
import org.sidis.lending.command.model.AuthorL;
import org.sidis.lending.command.model.BookL;
import org.sidis.lending.command.model.GenreL;
import org.sidis.lending.command.repositories.AuthorRepository;
import org.sidis.lending.command.repositories.BookRepository;
import org.sidis.lending.command.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;

    public BookServiceImpl(BookRepository bookRepository, GenreRepository genreRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
    }

    @Override
    public List<BookL> getAll() {
        System.out.println("BookServiceImpl getAll()");
        //return bookRepository.findAll();
        final List<BookL> a = bookRepository.findAll();
        System.out.println("BookServiceImpl bookRepository.findAll() result: " + a.size() + " " + a.get(0).getTitle());
        return a;
    }

    @Override
    public Optional<BookL> getBookById(final Long bookID) {
        Optional<BookL> book = bookRepository.findBookByBookID(bookID);


        if (book.isPresent()) {
            BookL foundBook = book.get();

            // Check and save the genre if it's transient
            GenreL genre = foundBook.getGenre();
            if (genre != null && genre.getId() == null) {genreRepository.save(genre);}

            // Check and save each author if they're transient
            List<AuthorL> authors = foundBook.getAuthor();
            List<AuthorL> savedAuthors = new ArrayList<>();
            for (AuthorL author : authors) {
            if (author.getAuthorID() == null) {
                author = authorRepository.save(author); // Save transient authors
            }
            savedAuthors.add(author);
            }
            foundBook.setAuthor(savedAuthors);

            // Save the book after ensuring all relationships are persisted
            bookRepository.save(foundBook);
        } else {throw new NotFoundException("Book not found in both instances for bookID: " + bookID);}
        return book;
    }

    @Override
    public Optional<BookL> getBookByIsbn(final String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<BookL> getBookByGenre(final String genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public List<BookL> getBookByTitle(final String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<BookL> getBooksByAuthorId(String authorID) {
        return bookRepository.findByAuthorId(authorID);
    }
}
