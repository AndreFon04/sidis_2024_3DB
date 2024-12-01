package org.sidis.user.command.client;

import org.sidis.user.command.client.GenreDTO;

public class BookDTO {

    private Long id;

    private GenreDTO genre;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public GenreDTO getGenre() {
        return genre;
    }

    public void setGenre(GenreDTO genre) {
        this.genre = genre;
    }

}
