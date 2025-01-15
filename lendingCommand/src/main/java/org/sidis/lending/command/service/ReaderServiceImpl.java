package org.sidis.lending.command.service;

import org.sidis.lending.command.model.ReaderL;
import org.sidis.lending.command.repositories.ReaderRepository;
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
    public List<ReaderL> findAll() {
        return readerRepository.findAll();
    }

    @Override
    public Optional<ReaderL> getReaderByID(final String readerID) {
        return readerRepository.findByReaderID(readerID);
    }

    @Override
    public Optional<ReaderL> getReaderByEmail(final String email) {
        return readerRepository.findByEmail(email);
    }

    @Override
    public List<ReaderL> getReaderByName(final String name) {
        return readerRepository.findByName(name);
    }

}
