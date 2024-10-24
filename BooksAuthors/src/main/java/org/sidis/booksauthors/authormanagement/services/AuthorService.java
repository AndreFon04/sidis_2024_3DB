package org.sidis.booksauthors.authormanagement.services;

import com.example.library.authormanagement.model.Author;
import com.example.library.authormanagement.model.CoAuthorDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AuthorService {

	List<Author> findByName(String name);

	Optional<Author> findByAuthorID(String authorID);

	Author create(CreateAuthorRequest request);

	Author partialUpdate(String authorID, EditAuthorRequest request, long parseLong);

	List<CoAuthorDTO> getCoAuthorsAndBooks(String authorId);

	Map<String, Long> getTop5AuthorsLendings();

}
