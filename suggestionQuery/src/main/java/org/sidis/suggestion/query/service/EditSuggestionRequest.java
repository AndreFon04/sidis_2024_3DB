package org.sidis.suggestion.query.service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
