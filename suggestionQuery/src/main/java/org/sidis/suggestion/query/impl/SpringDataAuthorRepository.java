package org.sidis.suggestion.query.impl;

import org.sidis.suggestion.query.model.AuthorS;
import org.sidis.suggestion.query.repositories.AuthorRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface SpringDataAuthorRepository extends AuthorRepository, CrudRepository<AuthorS, Long> {

    @Override
    @Query("SELECT a FROM AuthorS a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<AuthorS> findByName(@Param("name") String name);

    @Override
    @Query("SELECT a FROM AuthorS a WHERE a.authorID LIKE :authorID")
    Optional<AuthorS> findByAuthorID(@Param("authorID") String authorID);

    @Query("SELECT a FROM AuthorS a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%')) AND a.biography = :authorBiography")
    List<AuthorS> findByNameAndBiography(@Param("name") String name, @Param("authorBiography") String authorBiography);

    @Override
    @Query("SELECT a FROM AuthorS a ORDER BY substring(a.authorID, 1, 4), cast(substring(a.authorID, 6, 10) AS int) DESC LIMIT 1")
    Optional<AuthorS> findTopByOrderByAuthorIDDesc();

}
