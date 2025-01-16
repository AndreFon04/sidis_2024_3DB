package org.sidis.suggestion.query.repositories;

import org.sidis.suggestion.query.model.ReaderS;

import java.util.List;
import java.util.Optional;


public interface ReaderRepository {

    Optional<ReaderS> findTopByOrderByReaderIDDesc();

    Optional<ReaderS> findByReaderID(String readerID);

    Optional<ReaderS> findByEmail(String email);

    List<ReaderS> findByName(String name);

    List<ReaderS> findAll();

    <S extends ReaderS> S save(S entity);
}
