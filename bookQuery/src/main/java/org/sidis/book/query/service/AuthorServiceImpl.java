package org.sidis.book.query.service;

import org.sidis.book.query.client.LendingDTO;
import org.sidis.book.query.client.LendingServiceClient;
import org.sidis.book.query.exceptions.NotFoundException;
import org.sidis.book.query.model.Author;
import org.sidis.book.query.model.Book;
import org.sidis.book.query.model.TopAuthorLendingDTO;
import org.sidis.book.query.repositories.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;
    private final LendingServiceClient lendingServiceClient;
    private final BookService bookService;

    public AuthorServiceImpl(AuthorRepository authorRepository, LendingServiceClient lendingServiceClient, BookService bookService) {
        this.authorRepository = authorRepository;
        this.lendingServiceClient = lendingServiceClient;
        this.bookService = bookService;
    }

    @Override
    public List<Author> findByName(String name) {
        return authorRepository.findByName(name);
    }

    @Override
    public Optional<Author> findByAuthorID(String authorID) {
        return authorRepository.findByAuthorID(authorID);
    }




    public String getAuthorImageUrl(String authorID) {
        Author author = findByAuthorID(authorID).orElseThrow(() -> new NotFoundException(Author.class, authorID));
        if (author.getImage() != null && author.getImage().getAuthorImageID() != null) {
            String imageUrl = ServletUriComponentsBuilder
                    .fromCurrentContextPath()
                    .path("/api/authors/")
                    .path(authorID.replace("/", "_"))
                    .path("/photo/")
                    .path(author.getImage().getAuthorImageID().toString())
                    .toUriString();
            System.out.println("Generated Image URL: " + imageUrl);
            return imageUrl;
        }
        System.out.println("No Image Found for Author ID: " + authorID);
        return null;
    }

    public void saveAuthor(Author author) {
        authorRepository.save(author);
    }

    public List<TopAuthorLendingDTO> findTop5AuthorsPerLending() {
        List<LendingDTO> lendings = lendingServiceClient.getAllLendings();

        Map<Author, Long> authorLendingCounts = new HashMap<>();

        for (LendingDTO lending : lendings) {
            Book book = bookService.getBookById(lending.getBookID())  // Corrigido para pegar o bookID do LendingDTO
                    .orElseThrow(() -> new NotFoundException("Book not found with ID: " + lending.getBookID()));

            for (Author author : book.getAuthor()) {  // book.getAuthor() retorna a lista de autores
                authorLendingCounts.put(author, authorLendingCounts.getOrDefault(author, 0L) + 1);
            }
        }

        List<Map.Entry<Author, Long>> top5Authors = authorLendingCounts.entrySet().stream()
                .sorted(Map.Entry.<Author, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        return top5Authors.stream()
                .map(entry -> new TopAuthorLendingDTO(entry.getKey().getAuthorID(), entry.getKey().getName(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
