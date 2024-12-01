package org.sidis.user.query.repositories;

import org.sidis.user.query.model.Reader;
import org.sidis.user.query.service.Page;
import org.sidis.user.query.service.SearchReadersQuery;

import java.util.List;
import java.util.Optional;


public interface ReaderRepository {

    Optional<Reader> findTopByOrderByReaderIDDesc();

    boolean existsByEmail(String email);

    Optional<Reader> findByReaderID(String readerID);

    Optional<Reader> findByEmail(String email);

    List<Reader> findByName(String name);

    List<Reader> findAll();

    <S extends Reader> S save(S entity);

    List<Reader> searchReaders(Page page, SearchReadersQuery query);
}
