package org.sidis.user.query.service;

import org.mapstruct.*;
import org.sidis.user.query.model.Role;
import org.sidis.user.query.model.User;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;


@Mapper(componentModel = "spring")
public abstract class EditUserMapper {

	@Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringToRole")
	public abstract User create(CreateUserRequest request);

	@BeanMapping(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS, nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
	@Mapping(source = "authorities", target = "authorities", qualifiedByName = "stringToRole")
	@Mapping(target = "password", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	public abstract void update(EditUserRequest request, @MappingTarget User user);

	@Named("stringToRole")
	protected Set<Role> stringToRole(final Set<String> authorities) {
		if (authorities != null) {
			return authorities.stream().map(Role::new).collect(toSet());
		}
		return new HashSet<>();
	}
}
