package org.sidis.suggestion.command.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;


@Data
@Schema(description = "A Suggestion")
public class SuggestionView {
    private String suggestionID;
    private String bookISBN;
    private String bookTitle;
    private String bookAuthorName;
    private String readerID;
    private String notes;
}
