package org.sidis.lending.command.service;

import org.sidis.lending.command.model.ReaderL;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface ReaderService {

    List<ReaderL> findAll();

    Optional<ReaderL> getReaderByID(String readerID);

    Optional<ReaderL> getReaderByEmail(String email);

    List<ReaderL> getReaderByName(String name);
}
