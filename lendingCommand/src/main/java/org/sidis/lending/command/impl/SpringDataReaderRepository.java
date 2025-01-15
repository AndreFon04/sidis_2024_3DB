package org.sidis.lending.command.impl;

import org.sidis.lending.command.model.ReaderL;
import org.sidis.lending.command.repositories.ReaderRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpringDataReaderRepository extends ReaderRepository, CrudRepository<ReaderL, Long> {

	@Override
	@Query("SELECT r FROM ReaderL r ORDER BY substring(r.readerID, 1, 4), cast(substring(r.readerID, 6, 10) AS int) DESC LIMIT 1")
	Optional<ReaderL> findTopByOrderByReaderIDDesc();

	@Override
	@Query("SELECT r FROM ReaderL r WHERE r.readerID LIKE :readerID")
	Optional<ReaderL> findByReaderID(@Param("readerID") String readerID);

	@Override
	@Query("SELECT r FROM ReaderL r WHERE r.email LIKE :email")
	Optional<ReaderL> findByEmail(@Param("email") String email);

	@Query("SELECT COUNT(r) FROM ReaderL r")
	long count();

	@Override
	@Query("SELECT r FROM ReaderL r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<ReaderL> findByName(@Param("name") String name);
}
