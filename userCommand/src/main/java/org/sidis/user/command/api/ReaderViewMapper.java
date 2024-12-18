package org.sidis.user.command.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.sidis.user.command.model.Reader;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Mapper(componentModel = "spring")
public abstract class ReaderViewMapper {

    @Mapping(source = "readerImage.readerImageID", target = "imageUrl", qualifiedByName = "mapImageIdToUrl")
    public abstract ReaderView toReaderView(Reader reader);

    public abstract List<ReaderView> toReaderView(List<Reader> reader);

    public abstract Iterable<ReaderView> toReaderView(Iterable<Reader> reader);

    Set<String> map(Set<String> interests) {
        Set<String> result = new HashSet<>();
//        for (Genre genre : value){
//            result.add(genre.getInterest());
//        }
        return result;
    }

    @Named("mapImageIdToUrl")
    String mapImageIdToUrl(Long readerImageID) {
        return readerImageID != null ? "/api/readers/images/" + readerImageID : null;
    }
}
