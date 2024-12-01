package org.sidis.book.command.service;

import org.sidis.book.command.client.LendingServiceClient;
import org.sidis.book.command.exceptions.NotFoundException;
import org.sidis.book.command.message_broker.MessagePublisher;
import org.sidis.book.command.model.*;
import org.sidis.book.command.repositories.AuthorRepository;
import org.sidis.book.command.repositories.BookImageRepository;
import org.sidis.book.command.repositories.BookRepository;
import org.sidis.book.command.repositories.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookImageRepository bookImageRepository;
    private final AuthorRepository authorRepository;
    private final LendingServiceClient lendingServiceClient;
    private final GenreRepository genreRepository;
    private final MessagePublisher sender;

    public BookServiceImpl(BookRepository bookRepository, BookImageRepository bookImageRepository, GenreRepository genreRepository, AuthorRepository authorRepository, LendingServiceClient lendingServiceClient, MessagePublisher sender) {
        this.bookRepository = bookRepository;
        this.bookImageRepository = bookImageRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.lendingServiceClient = lendingServiceClient;
        this.sender = sender;
    }

    public Book create(CreateBookRequest request) {
        String isbn = request.getIsbn();
        Book book = new Book();
        if (!book.isValidIsbn(isbn)) {
            throw new IllegalArgumentException("Invalid ISBN");
        }

        // Fetch the Genre entity
        Genre genre = genreRepository.findByInterest(request.getGenre());
        if (genre == null) {
            throw new NotFoundException("Genre not found");
        }

        // Fetch the Author entities
        List<Author> authors = new ArrayList<>();
        for (String authorId : request.getAuthorIds()) {
            Author author = authorRepository.findByAuthorID(authorId)
                    .orElseThrow(() -> new NotFoundException("Author not found with ID: " + authorId));
            if (author.getAuthorID() == null) {
                author = authorRepository.save(author);
            }
            authors.add(author);
        }

        // Fetch the BookImage entity
        BookImage bookImage = bookImageRepository.findById(request.getBookImageId())
                .orElseThrow(() -> new NotFoundException("Book image not found"));

        // Create and save the new Book entity
        Book newBook = new Book();
        newBook.setIsbn(isbn);
        newBook.setTitle(request.getTitle());
        newBook.setGenre(genre);
        newBook.setDescription(request.getDescription());
        newBook.setAuthor(authors); // Set the list of authors
        newBook.setBookImage(bookImage);

        bookRepository.save(newBook);
        sender.publishBookCreated(newBook);

        return newBook;
    }

    @Override
    public Optional<Book> getBookById(final Long bookID) {
        Optional<Book> book = bookRepository.findBookByBookID(bookID);

        if (book.isEmpty()) {
            book = lendingServiceClient.getBookFromOtherInstance(bookID);

            if (book.isPresent()) {
                Book foundBook = book.get();

                // Check and save the genre if it's transient
                Genre genre = foundBook.getGenre();
                if (genre != null && genre.getId() == null) {
                    genreRepository.save(genre);
                }

                // Check and save each author if they're transient
                List<Author> authors = foundBook.getAuthor();
                List<Author> savedAuthors = new ArrayList<>();
                for (Author author : authors) {
                    if (author.getAuthorID() == null) {
                        author = authorRepository.save(author); // Save transient authors
                    }
                    savedAuthors.add(author);
                }
                foundBook.setAuthor(savedAuthors);

                // Save the book after ensuring all relationships are persisted
                bookRepository.save(foundBook);
            } else {
                throw new NotFoundException("Book not found in both instances for bookID: " + bookID);
            }
        }

        return book;
    }

    @Override
    public Book partialUpdate(final Long bookID, final EditBookRequest request, final long desiredVersion) {
        var existingBook = getBookById(bookID).orElseThrow(() -> new NotFoundException("Cannot update an object that does not yet exist"));

        Genre genre = genreRepository.findByInterest(request.getGenre());
        if (genre == null) {
            throw new NotFoundException("Genre not found");
        }

        existingBook.applyPatch(desiredVersion, request.getTitle(), genre, request.getDescription());
        bookRepository.save(existingBook);
        sender.publishBookUpdated(existingBook);

        return existingBook;
    }
}
