package org.sidis.user.readermanagement.services;

import com.example.library.readermanagement.model.Reader;
import com.example.library.usermanagement.services.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public interface ReaderService {

	List<Reader> findAll();

	Reader create(CreateReaderRequest request);

    Reader partialUpdate(String readerID, EditReaderRequest request, long parseLong);

	List<Reader> getTop5Readers();

	Optional<Reader> getReaderByID(String readerID);

	Optional<Reader> getReaderByEmail(String email);

	List<Reader> getReaderByName(final String name);

	Set<String> getInterestsByReader(Reader reader);

	List<Reader> searchReaders(Page page, SearchReadersQuery query);
}
