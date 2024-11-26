package org.sidis.book.command.api;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.sidis.book.command.api.BookView;
import org.sidis.book.command.model.Book;
import org.sidis.book.command.model.Genre;
import org.sidis.book.command.model.GenreBookCountDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Mapper(componentModel = "spring")
public interface BookViewMapper {

    @Mapping(source = "author", target = "author")
    @Mapping(source = "genre", target = "genre", qualifiedByName = "mapGenreToString")
    @Mapping(source = "bookImage.bookImageID", target = "imageUrl", qualifiedByName = "mapImageIdToUrl")
    BookView toBookView(Book book);

    List<BookView> toBookView(List<Book> books);

    @Named("mapGenreToString")
    default String mapGenreToString(Genre genre) {
        return genre != null ? genre.getInterest() : null;
    }


    default BookView mapToBookView(Map.Entry<String, Long> genreEntry) {
        BookView bookView = new BookView();
        bookView.setGenre(genreEntry.getKey());
        bookView.setBookCount(genreEntry.getValue());
        return bookView;
    }

    default List<BookView> mapTopGenresToBookViews(List<Map.Entry<String, Long>> genres) {
        return genres.stream().map(this::mapToBookView).collect(Collectors.toList());
    }
    default GenreBookCountDTO mapToGenreBookCountDTO(Map.Entry<String, Long> genreEntry) {
        return new GenreBookCountDTO(genreEntry.getKey(), genreEntry.getValue());
    }

    default List<GenreBookCountDTO> mapTopGenresToGenreBookCountDTOs(List<Map.Entry<String, Long>> genres) {
        return genres.stream().map(this::mapToGenreBookCountDTO).collect(Collectors.toList());
    }

    @Named("mapImageIdToUrl")
    default String mapImageIdToUrl(Long bookImageID) {
        return bookImageID != null ? "/api/books/images/" + bookImageID : null;
    }
}
