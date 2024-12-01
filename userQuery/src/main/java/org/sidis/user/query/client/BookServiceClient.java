package org.sidis.user.query.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;


@Service
public class BookServiceClient {

    private static final Logger log = LoggerFactory.getLogger(BookServiceClient.class);
    private final RestTemplate restTemplate;

//    @Value("${book.instance1.url}")
//    private String bookInstance1Url;
//
//    @Value("${book.instance2.url}")
//    private String bookInstance2Url;

    public BookServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
//        log.info("BookServiceClient URLs: instance1=" + bookInstance1Url + ", instance2=" + bookInstance2Url);
    }

    public List<GenreDTO> getBooksByGenre(String genre) {
//        List<GenreDTO> books = fetchBooksByGenre(bookInstance1Url + "/api/books/genre/" + genre);
//        if (books.isEmpty()) {
//            log.warn("Nenhum livro encontrado para o gênero: " + genre);
//        } else {
//            log.info("Livros retornados: " + books);
//        }
//        return books;
        return List.of();
    }


    private List<GenreDTO> fetchBooksByGenre(String url) {
        ResponseEntity<GenreDTO[]> response = restTemplate.getForEntity(url, GenreDTO[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            List<GenreDTO> books = Arrays.asList(response.getBody());
            log.info("Books received from API: " + books);
            return books;
        } else {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Unable to fetch books from " + url);
        }
    }

}
