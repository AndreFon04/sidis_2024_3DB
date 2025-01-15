package org.sidis.book.query.message_broker;

import lombok.RequiredArgsConstructor;
import org.sidis.book.query.model.Author;
import org.sidis.book.query.model.AuthorDTO;
import org.sidis.book.query.model.Book;
import org.sidis.book.query.repositories.AuthorRepository;
import org.sidis.book.query.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

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
    public void notifyB(Book book, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "book.created", "book.updated":
                logger.info("Received book with id: {}", book.getBookID());
                if(bookRepository.findBookByBookID(book.getBookID()).isEmpty()) {
                    Book b = new Book(book.getIsbn(), book.getTitle(), book.getGenre(),
                            book.getDescription(), book.getAuthor(), book.getBookImage());
                    b.setBookID(book.getBookID());
                    b.setVersion(book.getVersion());
                    logger.info("Calling bookRepository.save() with bookid: {}", book.getBookID());
                    bookRepository.save(b);
                    logger.info("Returned from bookRepository.save()");
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
        exit(1);
    }

    @RabbitListener(queues = "book.query.queue")
    public String handleLendingRequest(Long bookID, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);
        Optional<Book> b = bookRepository.findBookByBookID(bookID);
        logger.info("Received book request with id: {}", bookID);

        String isbn = null;
        if (b.isPresent()) {
            isbn = b.get().getIsbn();
        }

        logger.info("Sending response with isbn: {} --> ", isbn);
        return isbn;
    }
}
