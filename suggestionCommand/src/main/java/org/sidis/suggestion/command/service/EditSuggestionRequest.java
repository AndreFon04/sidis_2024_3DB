package org.sidis.suggestion.command.service;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditSuggestionRequest {

    @NotNull
    private String suggestionID;

    private String title;

    private String authorName;

    private String notes;
}
