package org.sidis.user.command.api;

import org.mapstruct.Mapper;
import org.sidis.user.command.api.UserView;
import org.sidis.user.command.model.User;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class UserViewMapper {

	public abstract UserView toUserView(User user);

	public abstract List<UserView> toUserView(List<User> users);
}
