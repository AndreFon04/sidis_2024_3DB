package org.sidis.lending.command.repositories;

import org.sidis.lending.command.model.ReaderL;

import java.util.List;
import java.util.Optional;


public interface ReaderRepository {

    Optional<ReaderL> findTopByOrderByReaderIDDesc();

    Optional<ReaderL> findByReaderID(String readerID);

    Optional<ReaderL> findByEmail(String email);

    List<ReaderL> findByName(String name);

    List<ReaderL> findAll();

    <S extends ReaderL> S save(S entity);
}
