package org.sidis.lending.command.dto;

import java.util.List;

public class BookDTO {
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private String description;

    public BookDTO(final String isbn, final String title, final String genre, final String description, final String author) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.author = author;
    }

    public String getIsbn() {return isbn;}
    public String getTitle() {return title;}
    public String getAuthor() {return author;}
    public String getGenre() {return genre;}
    public String getDescription() {return description;}


}
