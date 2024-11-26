package org.sidis.book.command.model;

import org.sidis.book.command.model.Book;

public class BookDTO {

    private Long bookId;
    private String title;

    public BookDTO(Book book) {
        this.bookId = book.getBookID();
        this.title = book.getTitle();
    }

    // Getters and setters

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
