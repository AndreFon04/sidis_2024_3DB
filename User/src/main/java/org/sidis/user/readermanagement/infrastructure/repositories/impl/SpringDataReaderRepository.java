package org.sidis.user.readermanagement.infrastructure.repositories.impl;

import com.example.library.readermanagement.model.Reader;
import com.example.library.readermanagement.repositories.ReaderRepository;
import com.example.library.readermanagement.services.SearchReadersQuery;
import com.example.library.usermanagement.services.Page;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface SpringDataReaderRepository extends ReaderRepository, ReaderRepoCustom, CrudRepository<Reader, Long> {

	@Override
	@Query("SELECT r FROM Reader r ORDER BY substring(r.readerID, 1, 4), cast(substring(r.readerID, 6, 10) AS int) DESC LIMIT 1")
	Optional<Reader> findTopByOrderByReaderIDDesc();

	@Override
	@Query(value = "SELECT r.* FROM Reader r, Lending l WHERE r.readerID = l.reader_id GROUP BY l.reader_id ORDER BY COUNT(l.reader_id) DESC LIMIT 5", nativeQuery = true)
	List<Reader> findTop5Readers();

	@Override
	@Query("SELECT r FROM Reader r WHERE r.readerID LIKE :readerID")
	Optional<Reader> findByReaderID(@Param("readerID") String readerID);

	@Override
	@Query("SELECT r FROM Reader r WHERE r.email LIKE :email")
	Optional<Reader> findByEmail(@Param("email") String email);

	@Query("SELECT COUNT(r) FROM Reader r")
	long count();

	@Override
	@Query("SELECT r FROM Reader r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
	List<Reader> findByName(@Param("name") String name);
}

interface ReaderRepoCustom {

	List<Reader> searchReaders(Page page, SearchReadersQuery query);
}

@RequiredArgsConstructor
class ReaderRepoCustomImpl implements ReaderRepoCustom {
	private final EntityManager em;

	@Override
	public List<Reader> searchReaders(final Page page, final SearchReadersQuery query) {

		final CriteriaBuilder cb = em.getCriteriaBuilder();
		final CriteriaQuery<Reader> cq = cb.createQuery(Reader.class);
		final Root<Reader> root = cq.from(Reader.class);
		cq.select(root);

		final List<Predicate> where = new ArrayList<>();
		if (StringUtils.hasText(query.getName())) {
			where.add(cb.like(cb.lower(root.get("name")), "%" + query.getName().toLowerCase() + "%"));
		}
		if (StringUtils.hasText(query.getEmail())) {
			where.add(cb.equal(root.get("email"), query.getEmail()));
		}
		if (StringUtils.hasText(query.getPhoneNumber())) {
			where.add(cb.like(root.get("phoneNumber"), query.getPhoneNumber()));
		}

		if (!where.isEmpty()) {	// search using OR
			cq.where(cb.or(where.toArray(new Predicate[0])));
		}

		cq.orderBy(cb.desc(root.get("createdAt")));

		final TypedQuery<Reader> q = em.createQuery(cq);
		q.setFirstResult((page.getNumber() - 1) * page.getLimit());
		q.setMaxResults(page.getLimit());

		return q.getResultList();
	}
}
