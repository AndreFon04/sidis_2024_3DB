package org.sidis.suggestion.query.service;

import org.sidis.suggestion.query.model.ReaderS;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface ReaderService {

    List<ReaderS> findAll();

    Optional<ReaderS> getReaderByID(String readerID);

    Optional<ReaderS> getReaderByEmail(String email);

    List<ReaderS> getReaderByName(String name);
}
