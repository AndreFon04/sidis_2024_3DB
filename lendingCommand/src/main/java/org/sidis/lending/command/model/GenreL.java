package org.sidis.lending.command.model;

import jakarta.persistence.*;
import org.sidis.lending.command.model.BookL;

import java.util.ArrayList;
import java.util.List;


@Entity
public class GenreL {


    public static final String ACAO = "Ação";
    public static final String FICCAO_CIENTIFICA = "Ficção Científica";
    public static final String ROMANCE = "Romance";
    public static final String MISTERIO = "Mistério";
    public static final String FANTASIA = "Fantasia";
    public static final String HISTORIA = "História";
    public static final String BIOGRAFIA = "Biografia";
    public static final String TERROR = "Terror";
    public static final String AVENTURA = "Aventura";
    public static final String DRAMA = "Drama";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String interest;

    @OneToMany(mappedBy = "genre", orphanRemoval = true,
            cascade = CascadeType.ALL)
    private List<BookL> books;

    public GenreL(String interest) {
        this.interest = interest;
        this.books = new ArrayList<>();
    }

    public GenreL() {}

    public Long getId() {
        return id;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public List<BookL> getBooks() {
        return books;
    }

    public void setBooks(List<BookL> books) {
        this.books = books;
    }
}
