package org.sidis.book.query.repositories;

import org.sidis.book.query.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAll();

    Genre findByInterest(String interest);

    Genre save(Genre genre);
}
