package org.sidis.user.readermanagement.services;

import com.example.library.bookmanagement.model.Genre;
import com.example.library.readermanagement.model.Reader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Mapper(componentModel = "spring")
public abstract class EditReaderMapper {

	@Mapping(source = "interests", target = "interests", qualifiedByName = "stringToGenre")
	public abstract Reader create(CreateReaderRequest request);

	@Mapping(target = "readerID", ignore = true)
	@Mapping(target = "enabled", ignore = true)
	@Mapping(source = "interests", target = "interests", qualifiedByName = "stringToGenre")
	public abstract void update(EditReaderRequest request, @MappingTarget Reader reader);

	@Named("stringToGenre")
	protected Set<Genre> stringToGenre(final Set<String> interests) {
		if (interests != null) {
			return interests.stream().map(Genre::new).collect(toSet());
		}
		return new HashSet<>();
	}
}
