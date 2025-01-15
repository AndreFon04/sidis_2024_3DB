package org.sidis.lending.command.dto;

import lombok.Getter;
import lombok.Setter;
import org.sidis.lending.command.model.AuthorL;
import org.sidis.lending.command.model.GenreL;

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
    private GenreL genre;
    @Setter
    private List<AuthorL> author = new ArrayList<>();


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
