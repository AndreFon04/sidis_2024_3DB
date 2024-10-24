package org.sidis.booksauthors.authormanagement.services;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor

public class CreateAuthorRequest{

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String biography;

    public CreateAuthorRequest(final String name, final String biography) {
        this.name = name;
        this.biography = biography;
    }
}
