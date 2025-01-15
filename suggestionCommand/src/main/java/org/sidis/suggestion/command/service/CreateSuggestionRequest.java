package org.sidis.suggestion.command.service;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CreateSuggestionRequest {

    @NotNull
    private String isbn;

    @NotNull
    private String title;

    @NotNull
    private String authorName;

    @NotNull
    private String readerID;

    private String notes;
}
