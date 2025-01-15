package org.sidis.suggestion.command.service;

import org.sidis.suggestion.command.model.ReaderS;
import org.sidis.suggestion.command.repositories.ReaderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ReaderServiceImpl implements ReaderService {

    private final ReaderRepository readerRepository;

    public ReaderServiceImpl(ReaderRepository readerRepository) {
        this.readerRepository = readerRepository;
    }

    @Override
    public List<ReaderS> findAll() {
        return readerRepository.findAll();
    }

    @Override
    public Optional<ReaderS> getReaderByID(final String readerID) {
        return readerRepository.findByReaderID(readerID);
    }

    @Override
    public Optional<ReaderS> getReaderByEmail(final String email) {
        return readerRepository.findByEmail(email);
    }

    @Override
    public List<ReaderS> getReaderByName(final String name) {
        return readerRepository.findByName(name);
    }

}
