package org.sidis.book.command.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.sidis.book.command.api.BookViewMapper;
import org.sidis.book.command.exceptions.NotFoundException;
import org.sidis.book.command.model.Book;
import org.sidis.book.command.model.BookCountDTO;
import org.sidis.book.command.model.BookImage;
import org.sidis.book.command.model.GenreBookCountDTO;
import org.sidis.book.command.repositories.BookImageRepository;
import org.sidis.book.command.repositories.BookRepository;
import org.sidis.book.command.service.BookServiceImpl;
import org.sidis.book.command.service.CreateBookRequest;
import org.sidis.book.command.service.EditBookRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpHeaders.IF_MATCH;


@Tag(name = "Books", description = "Endpoints for managing Books")
@RestController
@RequestMapping("/api/books")
class BookController {

    private static final Logger log = LoggerFactory.getLogger(BookController.class);
    private final BookServiceImpl bookService;

    private final BookRepository bookRepository;

    private final BookViewMapper bookMapper;

    private final BookImageRepository bookImageRepo;

    @Autowired
    public BookController(BookServiceImpl bookService, BookViewMapper bookMapper, BookImageRepository bookImageRepo, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookMapper = bookMapper;
        this.bookImageRepo = bookImageRepo;
        this.bookRepository = bookRepository;
    }

    @Operation(summary = "Creates a new Book")
    @PostMapping
    public ResponseEntity<BookView> createBook(@Valid @RequestBody CreateBookRequest request) {
        Book createdBook = bookService.create(request);
        return ResponseEntity.ok(bookMapper.toBookView(createdBook));
    }

    @PatchMapping(value = "/{bookID}")
    public ResponseEntity<BookView> partialUpdate(final WebRequest request,
                                                  @PathVariable("bookID") @Parameter(description = "The id of the book to update") final Long bookID,
                                                  @Valid @RequestBody final EditBookRequest resource) {
        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must issue a conditional PATCH using 'if-match'");
        }

        final var book = bookService.partialUpdate(bookID, resource, getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(book.getVersion())).body(bookMapper.toBookView(book));
    }

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }
}
