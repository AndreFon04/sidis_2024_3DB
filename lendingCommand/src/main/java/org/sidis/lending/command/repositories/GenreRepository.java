package org.sidis.lending.command.repositories;

import org.sidis.lending.command.model.GenreL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface GenreRepository extends JpaRepository<GenreL, Long> {

    List<GenreL> findAll();

    GenreL findByInterest(String interest);

    GenreL save(GenreL genre);
}
