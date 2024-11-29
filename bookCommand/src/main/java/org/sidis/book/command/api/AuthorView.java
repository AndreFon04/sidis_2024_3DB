package org.sidis.book.command.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.UUID;


@Data
@Schema(description = "An Author")
public class AuthorView {
    @Schema(description = "Author's name")
    private String name;

    @Schema(description = "Author's biography")
    private String biography;

    @Schema(description = "Author's ID")
    private UUID authorID;

    @Schema(description = "The image URL of the author")
    private String imageUrl;

    private UUID authorId;
}