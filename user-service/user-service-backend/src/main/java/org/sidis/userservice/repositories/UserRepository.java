package org.sidis.userservice.repositories;

import org.sidis.userservice.exceptions.NotFoundException;
import org.sidis.userservice.model.User;
import org.sidis.userservice.service.Page;
import org.sidis.userservice.service.SearchUsersQuery;

import java.util.List;
import java.util.Optional;


public interface UserRepository {

	<S extends User> List<S> saveAll(Iterable<S> entities);

	<S extends User> S save(S entity);

	Optional<User> findById(Long objectId);

	default User getById(final Long id) {
		final Optional<User> maybeUser = findById(id);
		// throws 404 Not Found if the user does not exist or is not enabled
		return maybeUser.filter(User::isEnabled).orElseThrow(() -> new NotFoundException(User.class, id));
	}

	Optional<User> findByUsername(String username);

	List<User> searchUsers(Page page, SearchUsersQuery query);
}