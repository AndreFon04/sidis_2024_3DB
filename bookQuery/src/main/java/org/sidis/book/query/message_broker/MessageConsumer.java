package org.sidis.book.query.message_broker;

import lombok.RequiredArgsConstructor;
import org.sidis.book.query.model.*;
import org.sidis.book.query.repositories.AuthorRepository;
import org.sidis.book.query.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.lang.System.exit;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final AuthorRepository repository;
    private final BookRepository bookRepository;

    @RabbitListener(queues = "#{authorQueue.name}")
    public void notify(AuthorDTO authorDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "author.created", "author.updated":
                logger.info("Received author with id: {}", authorDTO.getAuthorID());
                if(repository.findByAuthorID(authorDTO.getAuthorID()).isEmpty()) {
                    Author author = new Author(authorDTO.getName(), authorDTO.getBiography());
                    author.setAuthorID(authorDTO.getAuthorID());
                    repository.save(author);
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }

    @RabbitListener(queues = "#{bookQueue.name}")
    public void notifyB(BookDTO bookDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "book.created", "book.updated":
                logger.info("Received book with id: {}", bookDTO.getBookId());
                if(bookRepository.findBookByBookID(bookDTO.getBookId()).isEmpty()) {
                    List<Author> authorList = new ArrayList<>();
                    for (String authorID : bookDTO.getAuthors()) {
                        Author author = repository.findByAuthorID(authorID).orElse(null);
                        if (author != null) {
                            authorList.add(author);
                        }
                    }
                    Genre genre = new Genre(bookDTO.getGenre());
                    Book book = new Book(bookDTO.getIsbn(), bookDTO.getTitle(), genre, bookDTO.getDescription(), authorList, null);
                    book.setBookID(bookDTO.getBookId());
                    logger.info("Calling bookRepository.save() with bookid: {}", book.getBookID());
                    bookRepository.save(book);
                    logger.info("Returned from bookRepository.save()");
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
        exit(1);
    }
}
