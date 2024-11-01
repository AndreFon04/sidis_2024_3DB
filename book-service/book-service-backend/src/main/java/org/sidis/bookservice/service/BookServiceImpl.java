package org.sidis.bookservice.service;

import org.sidis.bookservice.client.LendingDTO;
import org.sidis.bookservice.client.LendingServiceClient;
import org.sidis.bookservice.exceptions.NotFoundException;
import org.sidis.bookservice.model.*;
import org.sidis.bookservice.repositories.AuthorRepository;
import org.sidis.bookservice.repositories.BookImageRepository;
import org.sidis.bookservice.repositories.BookRepository;
import org.sidis.bookservice.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookImageRepository bookImageRepository;

    private final AuthorRepository authorRepository;

    private final LendingServiceClient lendingServiceClient;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, BookImageRepository bookImageRepository, GenreRepository genreRepository, AuthorRepository authorRepository, LendingServiceClient lendingServiceClient) {
        this.bookRepository = bookRepository;
        this.bookImageRepository = bookImageRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.lendingServiceClient = lendingServiceClient;
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
        return newBook;
    }


    @Override
    public boolean isBookIDUnique(Long bookID) {
        // Check if readerID already exists
        return bookRepository.findBookByBookID(bookID).isEmpty();
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

    public List<Book> getAll() {
        return bookRepository.findAll();
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
    public Genre getGenreByInterest(String interest) {
        return genreRepository.findByInterest(interest);
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

    public void addImageToBook(Long bookID, byte[] image, String contentType) {
        Book book = bookRepository.findBookByBookID(bookID)
                .orElseThrow(() -> new NotFoundException("Book not found"));

        saveBookWithImage(book, image, contentType);
    }



    public void saveBookWithImage(Book book, byte[] image, String contentType) {
        Book savedBook = bookRepository.save(book); // Salva o livro no banco de dados

        BookImage bookImage = new BookImage();
        bookImage.setBook(savedBook); // Associa a imagem ao livro salvo
        bookImage.setImage(image);
        bookImage.setContentType(contentType);

        bookImageRepository.save(bookImage); // Salva a imagem no banco de dados
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
        return existingBook;
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
