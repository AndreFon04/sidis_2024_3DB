package org.sidis.book.query.service;

import org.sidis.book.query.client.LendingDTO;
import org.sidis.book.query.client.LendingServiceClient;
import org.sidis.book.query.exceptions.NotFoundException;
import org.sidis.book.query.model.*;
import org.sidis.book.query.repositories.AuthorRepository;
import org.sidis.book.query.repositories.BookImageRepository;
import org.sidis.book.query.repositories.BookRepository;
import org.sidis.book.query.repositories.GenreRepository;
import org.sidis.book.query.service.CreateBookRequest;
import org.sidis.book.query.service.EditBookRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookImageRepository bookImageRepository;

    private final AuthorRepository authorRepository;

    private final LendingServiceClient lendingServiceClient;

    public BookServiceImpl(BookRepository bookRepository, BookImageRepository bookImageRepository, GenreRepository genreRepository, AuthorRepository authorRepository, LendingServiceClient lendingServiceClient) {
        this.bookRepository = bookRepository;
        this.bookImageRepository = bookImageRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.lendingServiceClient = lendingServiceClient;
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
    public List<Book> getAll() {
        System.out.println("BookServiceImpl getAll()");
        //return bookRepository.findAll();
        final List<Book> a = bookRepository.findAll();
        System.out.println("BookServiceImpl bookRepository.findAll() result: " + a.size() + " " + a.get(0).getTitle());
        return a;
    }

    @Override
    public Optional<Book> getBookByIsbn(final String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Override
    public List<Book> getBookByGenre(final String genre) {
        return bookRepository.findByGenre(genre);
    }

    @Override
    public List<Book> getBookByTitle(final String title) {
        return bookRepository.findByTitle(title);
    }

    private GenreRepository genreRepository;

    @Override
    public List<Map.Entry<String, Long>> findTop5Genres() {
        List<Genre> genres = genreRepository.findAll();
        Map<String, Long> genreBookCount = new HashMap<>();

        for (Genre genre : genres) {
            long count = genre.getBooks().size();
            genreBookCount.put(genre.getInterest(), count);
        }

        return genreBookCount.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());
    }

    @Override
    public List<Book> getBooksByAuthorId(String authorID) {
        return bookRepository.findByAuthorId(authorID);
    }

    @Override
    public List<BookCountDTO> findTop5Books() {
        List<LendingDTO> lendings = lendingServiceClient.getAllLendings();

        Map<Long, Long> bookIdCounts = lendings.stream()
                .collect(Collectors.groupingBy(LendingDTO::getBookID, Collectors.counting()));

        List<Map.Entry<Long, Long>> top5Books = bookIdCounts.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        return top5Books.stream()
                .map(entry -> new BookCountDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
