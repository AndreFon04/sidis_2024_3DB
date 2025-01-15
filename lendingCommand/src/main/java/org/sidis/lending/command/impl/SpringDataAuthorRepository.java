package org.sidis.lending.command.impl;

import org.sidis.lending.command.model.AuthorL;
import org.sidis.lending.command.repositories.AuthorRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface SpringDataAuthorRepository extends AuthorRepository, CrudRepository<AuthorL, Long> {

    @Override
    @Query("SELECT a FROM AuthorL a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AuthorL> findByName(@Param("name") String name);

    @Override
    @Query("SELECT a FROM AuthorL a WHERE a.authorID LIKE :authorID")
    Optional<AuthorL> findByAuthorID(@Param("authorID") String authorID);

    @Query("SELECT a FROM AuthorL a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) AND a.biography = :authorBiography")
    List<AuthorL> findByNameAndBiography(@Param("name") String name, @Param("authorBiography") String authorBiography);

    @Override
    @Query("SELECT a FROM AuthorL a ORDER BY substring(a.authorID, 1, 4), cast(substring(a.authorID, 6, 10) AS int) DESC LIMIT 1")
    Optional<AuthorL> findTopByOrderByAuthorIDDesc();

}
