package org.sidis.bookservice.repositories;

import org.sidis.bookservice.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAll();

    Genre findByInterest(String interest);

    Genre save(Genre genre);
}
