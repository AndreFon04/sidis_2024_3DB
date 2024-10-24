package org.sidis.booksauthors.bookmanagement.services;

import com.example.library.authormanagement.model.Author;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;


@Data
public class EditBookAuthorsRequest {
    @NotNull
    @Size(min = 1)
    private List<Author> authors;
}
