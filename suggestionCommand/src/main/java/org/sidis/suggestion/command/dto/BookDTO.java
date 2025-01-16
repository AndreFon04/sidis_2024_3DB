package org.sidis.suggestion.command.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long bookId;
    private String title;
    private String isbn;
    private String description;
    private String genre;
    private List<String> authors = new ArrayList<>();
//    private BookImage bookImage;
    private int status;
}
