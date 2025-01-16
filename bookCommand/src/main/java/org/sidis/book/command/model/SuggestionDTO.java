package org.sidis.book.command.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SuggestionDTO {

    private String suggestionID;
    private String bookISBN;
    private String bookTitle;
    private String bookAuthorName;
    private String readerID;
    private String notes;
    private int state; // REJECTED = -1, PENDING = 0, APPROVED = 1
}
