package org.sidis.suggestion.query.repositories;

import org.sidis.suggestion.query.model.GenreS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GenreRepository extends JpaRepository<GenreS, Long> {

    List<GenreS> findAll();

    GenreS findByInterest(String interest);

    GenreS save(GenreS genre);
}
