package org.sidis.suggestion.command.service;

import org.sidis.suggestion.command.exceptions.NotFoundException;
import org.sidis.suggestion.command.model.AuthorS;
import org.sidis.suggestion.command.model.BookS;
import org.sidis.suggestion.command.model.GenreS;
import org.sidis.suggestion.command.repositories.AuthorRepository;
import org.sidis.suggestion.command.repositories.BookRepository;
import org.sidis.suggestion.command.repositories.GenreRepository;
import org.sidis.suggestion.command.service.BookService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


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
    public List<BookS> getAll() {
        System.out.println("BookServiceImpl getAll()");
        //return bookRepository.findAll();
        final List<BookS> a = bookRepository.findAll();
        System.out.println("BookServiceImpl bookRepository.findAll() result: " + a.size() + " " + a.get(0).getTitle());
        return a;
    }

    @Override
    public Optional<BookS> getBookById(final Long bookID) {
        Optional<BookS> book = bookRepository.findBookByBookID(bookID);


        if (book.isPresent()) {
            BookS foundBook = book.get();

            // Check and save the genre if it's transient
            GenreS genre = foundBook.getGenre();
            if (genre != null && genre.getId() == null) {genreRepository.save(genre);}

            // Check and save each author if they're transient
            List<AuthorS> authors = foundBook.getAuthor();
            List<AuthorS> savedAuthors = new ArrayList<>();
            for (AuthorS author : authors) {
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
    public Optional<BookS> getBookByIsbn(final String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<BookS> getBookByGenre(final String genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public List<BookS> getBookByTitle(final String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    public List<BookS> getBooksByAuthorId(String authorID) {
        return bookRepository.findByAuthorId(authorID);
    }
}
