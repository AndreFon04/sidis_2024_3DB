package org.sidis.suggestion.query.impl;

import org.sidis.suggestion.query.model.Suggestion;
import org.sidis.suggestion.query.repositories.SuggestionRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpringDataSuggestionRepository extends SuggestionRepository, CrudRepository<Suggestion, Long> {

    @Override
    @Query("SELECT s FROM Suggestion s ORDER BY substring(s.suggestionID, 1, 4), cast(substring(s.suggestionID, 6, 10) AS int) DESC LIMIT 1")
    Optional<Suggestion> findTopByOrderBySuggestionIDDesc();

    @Override
    @Query("SELECT s FROM Suggestion s WHERE s.suggestionID LIKE :suggestionID")
    Optional<Suggestion> findBySuggestionID(@Param("suggestionID") String suggestionID);

    @Override
    @Query("SELECT s FROM Suggestion s WHERE s.bookISBN LIKE :isbn")
    List<Suggestion> findByIsbn(@Param("isbn") String isbn);

    @Override
    @Query("SELECT s FROM Suggestion s WHERE s.readerID = :readerID")
    List<Suggestion> findByReaderID(@Param("readerID") String readerID);

    @Override
    @Query("SELECT s FROM Suggestion s")
    List<Suggestion> findAll();
}
