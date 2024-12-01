package org.sidis.user.query.service;

import org.sidis.user.query.model.Reader;
import org.sidis.user.query.model.ReaderCountDTO;
import org.sidis.user.query.service.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Service
public interface ReaderService {

    List<Reader> findAll();

    Reader saveReader(Reader reader);

//    List<ReaderCountDTO> findTop5Readers();

    Optional<Reader> getReaderByID(String readerID);

    Optional<Reader> getReaderByEmail(String email);

    List<Reader> getReaderByName(final String name);
}
