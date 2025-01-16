package org.sidis.book.query.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorDTO {

    private String name;
    private String biography;
    private String authorID;

    public AuthorDTO() {}

    public AuthorDTO(final String name, final String biography, final String authorID) {
        this.name = name;
        this.biography = biography;
        this.authorID = authorID;
    }

}
