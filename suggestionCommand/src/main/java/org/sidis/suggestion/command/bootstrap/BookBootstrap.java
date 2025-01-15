package org.sidis.suggestion.command.bootstrap;

import lombok.RequiredArgsConstructor;
import org.sidis.suggestion.command.model.AuthorS;
import org.sidis.suggestion.command.model.BookS;
import org.sidis.suggestion.command.model.GenreS;
import org.sidis.suggestion.command.repositories.AuthorRepository;
import org.sidis.suggestion.command.repositories.BookRepository;
import org.sidis.suggestion.command.repositories.GenreRepository;
import org.sidis.suggestion.command.service.AuthorServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
@RequiredArgsConstructor
//@Profile("bootstrap")
@Order(3)
public class BookBootstrap implements CommandLineRunner {

    @Qualifier("bookRepository")
    private final BookRepository bookRepo;
    private final GenreRepository genreRepo;
    private final AuthorRepository authorRepo;
    private final AuthorServiceImpl authorServiceImpl;
    private final ResourceLoader resourceLoader;

    @Override
    @Transactional
    public void run(final String... args) {
        System.out.println("BookBootstrapper running...");

        try {
            // Verify the last ID to initialize the counter if necessary
            final Optional<BookS> a = bookRepo.getLastId();
            if (a.isPresent()) {
                System.out.println(a.get().getBookID());
            }

            // Add the first book if it doesn't exist
            if (bookRepo.findByIsbn("1234567890").isEmpty()) {
                System.out.println("bkBt.b1");
                addBookIndividually("1234567891", "Aventura nas Estrelas", "Ficção Científica", "Uma jornada intergaláctica cheia de aventuras.",
                        List.of(new AuthorS("William Shakespeare", "England, 1564-1616")),
                        loadImage("assets/acao.jpg"), "image/jpeg");
                System.out.println("bkBt.b1.save");
            }

            // Add 20 books individually
            addBooksIndividually();
        } catch (Exception e) {
            System.err.println("Failed to execute BookBootstrapper: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("BookBootstrapper finished.");
    }

    private void addBooksIndividually() throws IOException {
        addBookIndividually("1234567892", "Romance Proibido", "Romance", "Um amor impossível em tempos difíceis.",
                List.of(new AuthorS("Jane Austen", "England, 1775-1817")), loadImage("assets/aventura.jpg"), "image/jpeg");

        addBookIndividually("1234567893", "Mistério na Biblioteca", "Mistério", "Um bibliotecário descobre segredos antigos.",
                List.of(new AuthorS("Mark Twain", "United States, 1835-1910")), loadImage("assets/fantasia.jpg"), "image/jpeg");

        addBookIndividually("1234567894", "O Reino Encantado", "Fantasia", "Uma terra mágica cheia de criaturas incríveis.",
                List.of(new AuthorS("Leo Tolstoy", "Russia, 1828-1910")), loadImage("assets/historia.jpg"), "image/jpeg");

        addBookIndividually("1234567895", "Guerra e Paz", "História", "Relatos de batalhas e suas consequências.",
                List.of(new AuthorS("Charles Dickens", "England, 1812-1870"), new AuthorS("Leo Tolstoy", "Russia, 1828-1910")), loadImage("assets/romance.jpg"), "image/jpeg");

        addBookIndividually("1234567896", "Vida de um Herói", "Biografia", "A história inspiradora de um verdadeiro herói.",
                List.of(new AuthorS("Homer", "Ancient Greece, c. 8th century BC"), new AuthorS("Mark Twain", "United States, 1835-1910")), loadImage("assets/acao.jpg"), "image/jpeg");

        addBookIndividually("1234567897", "Noite de Terror", "Terror", "Eventos assustadores em uma noite sombria.",
                List.of(new AuthorS("Gabriel Garcia Marquez", "Colombia, 1927-2014"), new AuthorS("Franz Kafka", "Austria-Hungary, 1883-1924"), new AuthorS("George Orwell", "India (British Raj), 1903-1950")), loadImage("assets/aventura.jpg"), "image/jpeg");

        addBookIndividually("1234567898", "Exploradores do Desconhecido", "Aventura", "Exploradores em busca de novos mundos.",
                List.of(new AuthorS("Franz Kafka", "Austria-Hungary, 1883-1924")), loadImage("assets/fantasia.jpg"), "image/jpeg");

        addBookIndividually("1234567899", "Dramas da Vida", "Drama", "Histórias emocionantes sobre desafios da vida.",
                List.of(new AuthorS("George Orwell", "India (British Raj), 1903-1950")), loadImage("assets/historia.jpg"), "image/jpeg");

        addBookIndividually("1234567880", "O Detetive Audacioso", "Ação", "Um detetive que não teme o perigo.",
                List.of(new AuthorS("Fyodor Dostoevsky", "Russia, 1821-1881")), loadImage("assets/romance.jpg"), "image/jpeg");

        addBookIndividually("1234567881", "Revolução Digital", "Ficção Científica", "A era das máquinas inteligentes.",
                List.of(new AuthorS("Herman Melville", "United States, 1819-1891")), loadImage("assets/acao.jpg"), "image/jpeg");

        addBookIndividually("1234567882", "Amor em Tempos de Guerra", "Romance", "Um romance florescendo em tempos de guerra.",
                List.of(new AuthorS("Virginia Woolf", "England, 1882-1941")), loadImage("assets/aventura.jpg"), "image/jpeg");

        addBookIndividually("1234567883", "O Enigma do Faraó", "Mistério", "Segredos antigos revelados em uma tumba.",
                List.of(new AuthorS("James Joyce", "Ireland, 1882-1941")), loadImage("assets/fantasia.jpg"), "image/jpeg");

        addBookIndividually("1234567884", "Magia e Feitiços", "Fantasia", "Histórias de magos e feitiços poderosos.",
                List.of(new AuthorS("Marcel Proust", "France, 1871-1922")), loadImage("assets/historia.jpg"), "image/jpeg");

        addBookIndividually("1234567885", "Impérios Caídos", "História", "A ascensão e queda de grandes impérios.",
                List.of(new AuthorS("Ernest Hemingway", "United States, 1899-1961")), loadImage("assets/romance.jpg"), "image/jpeg");

        addBookIndividually("1234567886", "Lendas de um Guerreiro", "Biografia", "A vida de um guerreiro lendário.",
                List.of(new AuthorS("William Shakespeare", "England, 1564-1616")), loadImage("assets/acao.jpg"), "image/jpeg");

        addBookIndividually("1234567887", "Pesadelo Vivo", "Terror", "Um pesadelo que se torna realidade.",
                List.of(new AuthorS("Jane Austen", "England, 1775-1817")), loadImage("assets/aventura.jpg"), "image/jpeg");

        addBookIndividually("1234567888", "Oceano Desconhecido", "Aventura", "Exploradores desvendando os mistérios do oceano.",
                List.of(new AuthorS("Mark Twain", "United States, 1835-1910")), loadImage("assets/fantasia.jpg"), "image/jpeg");

        addBookIndividually("1234567889", "Sob o Céu Cinzento", "Drama", "Histórias de superação em tempos difíceis.",
                List.of(new AuthorS("Leo Tolstoy", "Russia, 1828-1910")), loadImage("assets/historia.jpg"), "image/jpeg");

        addBookIndividually("1234567881023", "Missão Impossível", "Ação", "Uma missão cheia de desafios e perigos.",
                List.of(new AuthorS("Charles Dickens", "England, 1812-1870")), loadImage("assets/romance.jpg"), "image/jpeg");

        addBookIndividually("1234567881156", "O Código Secreto", "Ficção Científica", "Decifrando códigos numa sociedade futurista.",
                List.of(new AuthorS("Homer", "Ancient Greece, c. 8th century BC")), loadImage("assets/acao.jpg"), "image/jpeg");
    }


    private void addBookIndividually(String isbn, String title, String genreInterest, String description, List<AuthorS> authors, byte[] image, String contentType) {
        if (bookRepo.findByIsbn(isbn).isEmpty()) {
            try {
                List<AuthorS> authorList = new ArrayList<>();
                for (AuthorS author : authors) {
                    List<AuthorS> foundAuthors = authorRepo.findByNameAndBiography(author.getName(), author.getBiography());
                    if (!foundAuthors.isEmpty()) {
                        authorList.add(foundAuthors.get(0)); // Assuming we always take the first author returned
                    } else {
                        authorList.add(authorRepo.save(author)); // Save new author if not found
                    }
                }
                GenreS genre = genreRepo.findByInterest(genreInterest); // Get existing genre from DB
                if (genre == null) {
                    genre = new GenreS(genreInterest); // Create new genre if not found
                    genreRepo.save(genre);
                }
                BookS book = new BookS(isbn, title, genre, description, authorList);
                bookRepo.save(book);
                System.out.println("Book " + title + " saved successfully with authors " + authors.stream().map(AuthorS::getName).collect(Collectors.joining(", ")));
            } catch (Exception e) {
                System.err.println("Failed to add book " + title + ": " + e.getMessage());
                e.printStackTrace();
            }
        }
    }


    private byte[] loadImage(String imagePath) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + imagePath);
        try (InputStream is = resource.getInputStream();
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        } catch (FileNotFoundException e) {
            System.err.println("Image not found: " + imagePath);
            throw e;
        } catch (IOException e) {
            System.err.println("Failed to load image: " + imagePath);
            throw e;
        }
    }

}
