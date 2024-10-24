package org.sidis.user.readermanagement.repositories;

import com.example.library.readermanagement.model.Reader;
import com.example.library.readermanagement.services.SearchReadersQuery;
import com.example.library.usermanagement.services.Page;

import java.util.List;
import java.util.Optional;


public interface ReaderRepository {

	Optional<Reader> findTopByOrderByReaderIDDesc();

	List<Reader> findTop5Readers();

	Optional<Reader> findByReaderID(String readerID);

	Optional<Reader> findByEmail(String email);

	List<Reader> findByName(String name);

	List<Reader> findAll();

	<S extends Reader> S save(S entity);

	List<Reader> searchReaders(Page page, SearchReadersQuery query);
}
