package org.sidis.suggestion.query.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Entity
@Getter
@Setter
public class Suggestion {

    @Version
    private long version;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long pk; // database primary key

    @Column(unique = true, updatable = false, nullable = false)
    private String suggestionID;

    @Column(nullable = false)
    private String bookISBN;

    @Column(unique = false, updatable = true, nullable = false)
    private String bookTitle;

    @Column(unique = false, updatable = true, nullable = false)
    private String bookAuthorName;

    @Column(nullable = false)
    private String readerID;

    private String notes;

    private int state; // REJECTED = -1, PENDING = 0, APPROVED = 1

    private static int currentYear = Year.now().getValue();
    private static int counter = 0;

    public void initCounter(String lastSuggestionID) {
        if (lastSuggestionID != null && !lastSuggestionID.isBlank()) {
            // Split the lastSuggestionID into year and counter
            String[] parts = lastSuggestionID.split("/");
            if (parts.length == 2) {
                currentYear = Integer.parseInt(parts[0]);
                counter = Integer.parseInt(parts[1]);
            }
        }
    }

    private String generateUniqueSuggestionID() {
        if (Year.now().getValue() != currentYear) {
            currentYear = Year.now().getValue();
            counter = 0;
        }

        counter++;
        String idCounter = String.format("%d", counter);
        return currentYear + "/" + idCounter;
    }



    public Suggestion() {}

    public Suggestion(final String bookISBN, final String bookTitle,
                      final String bookAuthorName, final String readerID,
                      final int state) {
        this.bookISBN = bookISBN;
        this.bookTitle = bookTitle;
        this.bookAuthorName = bookAuthorName;
        this.readerID = readerID;
        this.state = state;
    }

    public void setUniqueSuggestionID() {
        this.suggestionID = generateUniqueSuggestionID();
    }



}
