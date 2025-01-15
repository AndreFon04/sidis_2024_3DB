package org.sidis.suggestion.command.dto;

import lombok.Getter;
import lombok.Setter;
import org.sidis.suggestion.command.model.AuthorS;
import org.sidis.suggestion.command.model.GenreS;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BookDTO {

    @Setter
    private Long bookId;
    private String title;
    @Setter
    private String isbn;
    @Setter
    private String description;
    @Setter
    private GenreS genre;
    @Setter
    private List<AuthorS> author = new ArrayList<>();


    public BookDTO() {}

    public BookDTO(final String isbn, final String title, final String description, final Long bookId) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.description = description;
    }

    public void setTitle(final String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be null, nor blank");
        }
        this.title = title;
    }
}
