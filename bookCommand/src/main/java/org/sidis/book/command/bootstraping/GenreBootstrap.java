package org.sidis.book.command.bootstraping;

import lombok.RequiredArgsConstructor;
import org.sidis.book.command.model.Genre;
import org.sidis.book.command.repositories.GenreRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


@Component
@RequiredArgsConstructor
//@Profile("bootstrap")
@Order(2)
public class GenreBootstrap implements CommandLineRunner {

    private final GenreRepository genreRepo;

    @Override
    public void run(String... args) {
        System.out.println("GenreBootstrapper.run");

        List<Genre> genres = Arrays.asList(
                new Genre(Genre.ACAO),
                new Genre(Genre.FICCAO_CIENTIFICA),
                new Genre(Genre.ROMANCE),
                new Genre(Genre.MISTERIO),
                new Genre(Genre.FANTASIA),
                new Genre(Genre.HISTORIA),
                new Genre(Genre.BIOGRAFIA),
                new Genre(Genre.TERROR),
                new Genre(Genre.AVENTURA),
                new Genre(Genre.DRAMA)
        );

        // So deve fazer save se ainda não existirem na db !!!
        if (genreRepo.count() == 0) {
            genreRepo.saveAll(genres);
        }
        System.out.println("Genres initialized");
    }
}
