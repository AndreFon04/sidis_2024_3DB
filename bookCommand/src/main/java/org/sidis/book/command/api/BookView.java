package org.sidis.book.command.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.sidis.book.command.model.Author;

import java.util.List;


@Data
@Schema(description = "A Book")
public class BookView {
    @Schema(description = "The ISBN of the book")
    private String isbn;

    @Schema(description = "The title of the book")
    private String title;

    @Schema(description = "The genre of the book")
    private String genre;

    @Schema(description = "The description of the book")
    private String description;

    @Schema(description = "The author(s) of the book")
    private List<Author> author;

    @Schema(description = "The image URL of the book")
    private String imageUrl;

    @Schema(description = "The ID of the book")
    private Long bookID;

    @Setter
    @Getter
    private Long bookCount;

    private int bookStatus;

}
