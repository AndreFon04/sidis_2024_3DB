package org.sidis.user.command.service;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.sidis.user.command.model.Reader;
import org.sidis.user.command.service.CreateReaderRequest;
import org.sidis.user.command.service.EditReaderRequest;

import java.util.HashSet;
import java.util.Set;


@Mapper(componentModel = "spring")
public abstract class EditReaderMapper {

    @Mapping(source = "interests", target = "interests", qualifiedByName = "stringToInterests")
    public abstract Reader create(CreateReaderRequest request);

    @Mapping(target = "readerID", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(source = "interests", target = "interests", qualifiedByName = "stringToInterests")
    public abstract void update(EditReaderRequest request, @MappingTarget Reader reader);

    @Named("stringToInterests")
    protected Set<String> stringToInterests(final Set<String> interests) {
        if (interests != null) {
            return new HashSet<>(interests);
        }
        return new HashSet<>();
    }
}
