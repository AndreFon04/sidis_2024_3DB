package org.sidis.user.query.api;

import org.mapstruct.Mapper;
import org.sidis.user.query.api.UserView;
import org.sidis.user.query.model.User;

import java.util.List;


@Mapper(componentModel = "spring")
public abstract class UserViewMapper {

	public abstract UserView toUserView(User user);

	public abstract List<UserView> toUserView(List<User> users);
}
