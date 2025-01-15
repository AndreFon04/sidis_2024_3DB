package org.sidis.suggestion.command.impl;

import org.sidis.suggestion.command.model.ReaderS;
import org.sidis.suggestion.command.repositories.ReaderRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpringDataReaderRepository extends ReaderRepository, CrudRepository<ReaderS, Long> {

	@Override
	@Query("SELECT r FROM ReaderS r ORDER BY substring(r.readerID, 1, 4), cast(substring(r.readerID, 6, 10) AS int) DESC LIMIT 1")
	Optional<ReaderS> findTopByOrderByReaderIDDesc();

	@Override
	@Query("SELECT r FROM ReaderS r WHERE r.readerID LIKE :readerID")
	Optional<ReaderS> findByReaderID(@Param("readerID") String readerID);

	@Override
	@Query("SELECT r FROM ReaderS r WHERE r.email LIKE :email")
	Optional<ReaderS> findByEmail(@Param("email") String email);

	@Query("SELECT COUNT(r) FROM ReaderS r")
	long count();

	@Override
	@Query("SELECT r FROM ReaderS r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<ReaderS> findByName(@Param("name") String name);
}
