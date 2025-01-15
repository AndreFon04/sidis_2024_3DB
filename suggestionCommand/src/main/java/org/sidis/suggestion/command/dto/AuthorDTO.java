package org.sidis.suggestion.command.dto;

import lombok.Getter;
import lombok.Setter;

public class AuthorDTO {

    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String biography;
    @Getter
    @Setter
    private String authorID;

    public AuthorDTO() {}

    public AuthorDTO(final String name, final String biography, final String authorID) {
        this.name = name;
        this.biography = biography;
        this.authorID = authorID;
    }

}
