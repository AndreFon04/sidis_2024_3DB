package org.sidis.bookservice.repositories;

import org.sidis.bookservice.model.BookImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface BookImageRepository extends JpaRepository<BookImage, Long>  {

    Optional<BookImage> findById(Long id);

    BookImage save(BookImage bookImage);
}