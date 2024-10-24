package org.sidis.user.usermanagement.services;

import com.example.library.usermanagement.model.Role;
import com.example.library.usermanagement.model.User;
import org.mapstruct.*;

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
