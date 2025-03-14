package org.sidis.book.command.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.sidis.book.command.model.Author;
import org.sidis.book.command.service.AuthorServiceImpl;
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


@Tag(name = "Authors", description = "Endpoints for managing Authors")
@RestController
@RequestMapping("/api/authors")
public class AuthorController {

    private static final String IF_MATCH = "If-Match";

    private final AuthorServiceImpl authorService;

    private static final Logger logger = LoggerFactory.getLogger(AuthorController.class);


    @Autowired
    public AuthorController(AuthorServiceImpl authorService) {
        this.authorService = authorService;
    }


    @Operation(summary = "Creates a new Author")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Author> create(HttpServletRequest HTTPRequest,
                                         @Valid @RequestBody final CreateAuthorRequest request) {

        final var author = authorService.create(request);
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
}

