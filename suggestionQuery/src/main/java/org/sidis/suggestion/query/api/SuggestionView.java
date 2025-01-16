package org.sidis.suggestion.query.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "A Suggestion")
public class SuggestionView {
    private String suggestionID;
    private String bookISBN;
    private String bookTitle;
    private String bookAuthorName;
    private String readerID;
    private String notes;
    private int state;
}
