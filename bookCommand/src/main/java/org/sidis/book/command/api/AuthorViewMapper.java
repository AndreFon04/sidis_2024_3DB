package org.sidis.book.command.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sidis.book.command.model.Author;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AuthorViewMapper {

    @Mapping(source = "authorID", target = "authorID")
    @Mapping(target = "imageUrl", expression = "java(imageUrl(author))")
    AuthorView toAuthorView(Author author);

    List<AuthorView> toAuthorView(List<Author> authors);

    default String imageUrl(Author author) {
        if (author.getImage() != null && author.getImage().getAuthorImageID() != null) {
            return ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/authors/")
                    .path(author.getAuthorID().replace("/", "_"))
                    .path("/photo/")
                    .path(author.getImage().getAuthorImageID().toString())
                    .toUriString();
        } else {
            return null;
        }
    }
}
