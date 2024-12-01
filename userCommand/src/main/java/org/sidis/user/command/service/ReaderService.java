package org.sidis.user.command.service;

import org.sidis.user.command.model.Reader;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public interface ReaderService {

    Reader create(CreateReaderRequest request);

    Reader partialUpdate(String readerID, EditReaderRequest request, long parseLong);

    Optional<Reader> getReaderByID(String readerID);

    List<Reader> searchReaders(Page page, SearchReadersQuery query);
}
