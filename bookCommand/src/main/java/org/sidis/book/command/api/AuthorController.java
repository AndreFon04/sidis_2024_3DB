package org.sidis.book.command.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.sidis.book.command.api.AuthorView;
import org.sidis.book.command.api.AuthorViewMapper;
import org.sidis.book.command.api.BookViewMapper;
import org.sidis.book.command.exceptions.NotFoundException;
import org.sidis.book.command.model.Author;
import org.sidis.book.command.model.CoAuthorDTO;
import org.sidis.book.command.model.TopAuthorLendingDTO;
import org.sidis.book.command.service.AuthorServiceImpl;
import org.sidis.book.command.service.BookServiceImpl;
import org.sidis.book.command.service.CreateAuthorRequest;
import org.sidis.book.command.service.EditAuthorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;


@Tag(name = "Authors", description = "Endpoints for managing Authors")
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private static final String IF_MATCH = "If-Match";

    private final AuthorServiceImpl authorService;
    private final Helper helper;

    private final BookServiceImpl bookService;
    private final BookViewMapper bookViewMapper;

    private final AuthorViewMapper authorViewMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);


    @Autowired
    public AuthorController(AuthorServiceImpl authorService, Helper helper, BookServiceImpl bookService, BookViewMapper bookViewMapper, AuthorViewMapper authorViewMapper) {
        this.authorService = authorService;
        this.helper = helper;
        this.bookService = bookService;
        this.bookViewMapper = bookViewMapper;
        this.authorViewMapper = authorViewMapper;
    }

//    @Operation(summary = "Gets a specific Author by Name")
//    @GetMapping(value = "/name/{name}")
//    public List<AuthorView> findByName(
//            @PathVariable("name") @Parameter(description = "The Name of the Author to find") final String name) {
//        List<Author> authors = authorService.findByName(name);
//        return authors.stream().map(authorViewMapper::toAuthorView).toList();
//    }
//
//    @Operation(summary = "Gets a specific Author by id")
//    @GetMapping(value = "/id/{id1}/{id2}")
//    public ResponseEntity<AuthorView> findByAuthorID(
//            @PathVariable("id1") @Parameter(description = "The id of the author to find") final String id1,
//            @PathVariable("id2") final String id2) {
//        String authorID = id1 + "/" + id2;
//        final var author = authorService.findByAuthorID(authorID).orElseThrow(() -> new NotFoundException(Author.class, authorID));
//        AuthorView authorView = authorViewMapper.toAuthorView(author);
//        authorView.setImageUrl(authorService.getAuthorImageUrl(authorID));
//        System.out.println("Author ID: " + authorID + " Image URL: " + authorView.getImageUrl());
//        return ResponseEntity.ok(authorView);
//    }

    @Operation(summary = "Creates a new Author")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Author> create(HttpServletRequest HTTPrequest,
                                         @Valid @RequestBody final CreateAuthorRequest request) {
        UUID authorID = helper.getUserByToken(HTTPrequest);

        final var author = authorService.create(request, authorID);
        return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(author);
    }

    @Operation(summary = "Partially updates an existing author")
    @PatchMapping(value = "/{id1}/{id2}")
    public ResponseEntity<Author> partialUpdate(final WebRequest request,
                                                @PathVariable("id1") @Parameter(description = "The id of the author to update") final String id1,
                                                @PathVariable("id2") final String id2,
                                                @Valid @RequestBody final EditAuthorRequest resource) {
        String authorID = id1 + "/" + id2;

        // Validar se o user autenticado tem o mesmo authorID que o authorID acima
        // se não, é FORBIDDEN

        final String ifMatchValue = request.getHeader(IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You must issue a conditional PATCH using 'if-match'");
        }

        final var author = authorService.partialUpdate(authorID, resource, getVersionFromIfMatchHeader(ifMatchValue));
        return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(author);
    }

    private Long getVersionFromIfMatchHeader(final String ifMatchHeader) {
        if (ifMatchHeader.startsWith("\"")) {
            return Long.parseLong(ifMatchHeader.substring(1, ifMatchHeader.length() - 1));
        }
        return Long.parseLong(ifMatchHeader);
    }


    //SprintB
    //SprintB
    //SprintB
    //SprintB

//    @Operation(summary = "Get coauthors")
//    @GetMapping("/coauthors/{id1}/{id2}/")
//    public ResponseEntity<List<CoAuthorDTO>> getCoAuthorsAndBooks(@PathVariable String id1, @PathVariable String id2) {
//        String authorId = id1 + "/" + id2;
//
//        List<CoAuthorDTO> coAuthors = authorService.getCoAuthorsAndBooks(authorId);
//        return ResponseEntity.ok(coAuthors);
//    }
//
//    @GetMapping("/top5Authors")
//    public List<TopAuthorLendingDTO> getTop5Authors() {
//        return authorService.findTop5AuthorsPerLending();
//    }

}

