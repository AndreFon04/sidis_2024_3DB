package org.sidis.book.command.message_broker;

import org.sidis.book.command.model.Author;
import org.sidis.book.command.model.Book;
import org.sidis.book.command.repositories.AuthorRepository;
import org.sidis.book.command.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final AuthorRepository repository;
    private final BookRepository bookRepository;

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
//                    author.initCounter(author.getAuthorID());
                    bookRepository.save(book);
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }
}
