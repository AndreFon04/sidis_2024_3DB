package org.sidis.lending.command.dto;

import jakarta.persistence.*;

@Entity
public class GenreDTO {


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


    public GenreDTO(String interest) {
        this.interest = interest;
    }

    public GenreDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getInterest() {
        return interest;
    }

}
