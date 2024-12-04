package org.sidis.book.query.message_broker;

import lombok.RequiredArgsConstructor;
import org.sidis.book.query.model.Author;
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

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final AuthorRepository repository;
    private final BookRepository bookRepository;
    private final RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = "#{authorQueue.name}")
    public void notify(Author author, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "author.created", "author.updated":
                logger.info("Received author with id: {}", author.getAuthorID());
                if(repository.findByAuthorID(author.getAuthorID()).isEmpty()) {
                    // restart static internal ID
                    author.initCounter(author.getAuthorID());
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
                    bookRepository.save(book);
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
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
