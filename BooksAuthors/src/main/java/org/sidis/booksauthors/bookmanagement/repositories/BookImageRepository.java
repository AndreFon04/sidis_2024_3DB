package org.sidis.booksauthors.bookmanagement.repositories;

import com.example.library.bookmanagement.model.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long> {

	Optional<BookImage> findById(Long id);


	BookImage save(BookImage bookImage);
}
