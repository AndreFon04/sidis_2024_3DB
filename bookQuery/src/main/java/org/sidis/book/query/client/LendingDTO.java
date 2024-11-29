package org.sidis.book.query.client;

public class LendingDTO {

    private String lendingID;
    private Long bookID;


    public String getLendingID() {
        return lendingID;
    }

    public void setLendingID(String lendingID) {
        this.lendingID = lendingID;
    }

    public Long getBookID() {
        return bookID;
    }

    public void setBookID(Long bookID) {
        this.bookID = bookID;
    }
}
