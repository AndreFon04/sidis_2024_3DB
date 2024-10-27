package org.sidis.userservice.repositories;

import org.sidis.userservice.model.Reader;
import org.sidis.userservice.service.Page;
import org.sidis.userservice.service.SearchReadersQuery;

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
