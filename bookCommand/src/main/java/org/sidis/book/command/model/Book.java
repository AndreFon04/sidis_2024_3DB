package org.sidis.book.command.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.List;


@Entity
public class Book {
    @Setter
    @Version
    private long version;

    @Setter
    @Getter
    @Column(unique = true, updatable = false, nullable = false)
    private String isbn;

    @Getter
    @Column(unique = false, updatable = true, nullable = false)
    private String title;

    @Getter
    @ManyToOne
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    @Getter
    @Column(unique = false, updatable = true, nullable = false)
    private String description;

    @Setter
    @Getter
    @ManyToMany
    @JoinTable(
            name = "book_author",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> author = new ArrayList<>();

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "bookimage_id")
    private BookImage bookImage;

    @Getter
    @Id
    @GeneratedValue
    @Column(unique = true, updatable = false, nullable = false)
    private Long bookID;

    @Getter
    @Setter
    private int bookStatus; // -1 - Cancelled | 0 - Suggested | 1 - In library

    public Book() {}

    public Book(final String isbn, final String title, final Genre genre, final String description, final List<Author> author, final BookImage bookImage, final int bookStatus) {
        this.isbn = isbn;
        this.title = title;
        this.genre = genre;
        this.description = description;
        this.author = author;
        this.bookImage = bookImage;
        this.bookStatus = bookStatus;
    }


    public void setGenre(final Genre genre) {
        if (genre == null || genre.getInterest().isBlank()) {
            throw new IllegalArgumentException("Genre must not be null, nor blank");
        }
        this.genre = genre;
    }

    public void setTitle(final String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be null, nor blank");
        }
        this.title = title;
    }

    public void setDescription(final String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be null, nor blank");
        }
        this.description = description;
    }

    public Long getVersion() {
        return version;
    }

    public void applyPatch(long desiredVersion, String title, Genre genre, String description) {
        if (this.version != desiredVersion) {
            throw new OptimisticLockingFailureException("The entity was updated by another transaction");
        }
        this.title = title;
        this.genre = genre;
        this.description = description;
    }

    public void setBookID(long bookID) {
        this.bookID=bookID;
    }

    public boolean isValidISBN13(String isbn) {
        if (isbn == null || isbn.length() != 13) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            if (digit < 0 || digit > 9) {
                return false;
            }
            sum += (i % 2 == 0) ? digit : digit * 3;
        }
        int checkDigit = Character.getNumericValue(isbn.charAt(12));
        int calculatedCheckDigit = 10 - (sum % 10);
        if (calculatedCheckDigit == 10) {
            calculatedCheckDigit = 0;
        }
        return checkDigit == calculatedCheckDigit;
    }

    public boolean isValidISBN10(String isbn) {
        if (isbn == null || isbn.length() != 10) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            if (digit < 0 || digit > 9) {
                return false;
            }
            sum += (i + 1) * digit;
        }
        char lastChar = isbn.charAt(9);
        if (lastChar == 'X') {
            sum += 10;
        } else {
            int lastDigit = Character.getNumericValue(lastChar);
            if (lastDigit < 0 || lastDigit > 9) {
                return false;
            }
            sum += 10 * lastDigit;
        }
        return sum % 11 == 0;
    }


    public boolean isValidIsbn(String isbn) {
        if (isbn == null || isbn.isEmpty()) {
            return false;
        }
        // Remove spaces and dashes
        isbn = isbn.replaceAll("[\\s-]", "");
        // Check if the length is valid for ISBN-10 or ISBN-13
        if (isbn.length() != 10 && isbn.length() != 13) {
            return false;
        }
        // Check if the ISBN-10 or ISBN-13 format is valid
        if (isbn.length() == 10) {
            return isValidISBN10(isbn);
        } else {
            return isValidISBN13(isbn);
        }
    }

}
