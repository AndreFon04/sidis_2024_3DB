package org.sidis.book.query.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sidis.book.query.exceptions.NotFoundException;
import org.sidis.book.query.model.Author;
import org.sidis.book.query.model.TopAuthorLendingDTO;
import org.sidis.book.query.service.AuthorServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Authors", description = "Endpoints for managing Authors")
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private static final String IF_MATCH = "If-Match";

    private final AuthorServiceImpl authorService;
    private final AuthorViewMapper authorViewMapper;

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);


    @Autowired
    public AuthorController(AuthorServiceImpl authorService, AuthorViewMapper authorViewMapper) {
        this.authorService = authorService;
        this.authorViewMapper = authorViewMapper;
    }

    @Operation(summary = "Gets a specific Author by Name")
    @GetMapping(value = "/name/{name}")
    public List<AuthorView> findByName(
            @PathVariable("name") @Parameter(description = "The Name of the Author to find") final String name) {
        List<Author> authors = authorService.findByName(name);
        return authors.stream().map(authorViewMapper::toAuthorView).toList();
    }

    @Operation(summary = "Gets a specific Author by id")
    @GetMapping(value = "/id/{id1}/{id2}")
    public ResponseEntity<AuthorView> findByAuthorID(
            @PathVariable("id1") @Parameter(description = "The id of the author to find") final String id1,
            @PathVariable("id2") final String id2) {
        String authorID = id1 + "/" + id2;
        final var author = authorService.findByAuthorID(authorID).orElseThrow(() -> new NotFoundException(Author.class, authorID));
        AuthorView authorView = authorViewMapper.toAuthorView(author);
        authorView.setImageUrl(authorService.getAuthorImageUrl(authorID));
        System.out.println("Author ID: " + authorID + " Image URL: " + authorView.getImageUrl());
        return ResponseEntity.ok(authorView);
    }

    @GetMapping("/top5Authors")
    public List<TopAuthorLendingDTO> getTop5Authors() {
        return authorService.findTop5AuthorsPerLending();
    }
}

