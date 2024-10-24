package org.sidis.booksauthors.authormanagement.repositories;

import com.example.library.authormanagement.model.Author;
import com.example.library.bookmanagement.model.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

	List<Author> findByName(String name);

	Optional<Author> findByAuthorID(String authorID);

	List<Author> findByNameAndBiography(String name, String authorBiography);

	<S extends Author> S save(S entity);

	Optional<Author> findTopByOrderByAuthorIDDesc();

	List<Object[]> getTop5AuthorsLending(Pageable pageable);

	List<Book> findByAuthorsContaining(Author author);

}
