package org.sidis.suggestion.command.message_broker;

import lombok.RequiredArgsConstructor;
import org.sidis.suggestion.command.dto.AuthorDTO;
import org.sidis.suggestion.command.dto.BookDTO;
import org.sidis.suggestion.command.dto.ReaderDTO;
import org.sidis.suggestion.command.model.AuthorS;
import org.sidis.suggestion.command.model.BookS;
import org.sidis.suggestion.command.model.Suggestion;
import org.sidis.suggestion.command.model.ReaderS;
import org.sidis.suggestion.command.repositories.AuthorRepository;
import org.sidis.suggestion.command.repositories.BookRepository;
import org.sidis.suggestion.command.repositories.SuggestionRepository;
import org.sidis.suggestion.command.repositories.ReaderRepository;
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
    private final SuggestionRepository suggestionRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ReaderRepository readerRepository;

    @RabbitListener(queues = "suggestion.queue")
    public void notify(Suggestion suggestion, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "suggestion.created", "suggestion.updated":
                logger.info("Received suggestion with id: {}", suggestion.getSuggestionID());
                if(suggestionRepository.findBySuggestionID(suggestion.getSuggestionID()).isEmpty()) {
                    // restart static internal ID
                    logger.info("About to save suggestion");
                    suggestionRepository.save(suggestion);
                    logger.info("Saved suggestion");
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }

    @RabbitListener(queues = "book.queue")
    public void notifyBook(BookDTO bookDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "book.created", "book.updated":
                logger.info("Received book with isbn: {}", bookDTO.getIsbn());
                if(bookRepository.findByIsbn(bookDTO.getIsbn()).isEmpty()) {
                    BookS b = new BookS(bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getGenre(), bookDTO.getDescription(), bookDTO.getAuthor());
                    b.setBookID(bookDTO.getBookId());
                    logger.info("About to save book");
                    bookRepository.save(b);
                    logger.info("Book saved with isbn: {}", b.getIsbn());
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }

    @RabbitListener(queues = "author.queue")
    public void notifyAuthor(AuthorDTO authorDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "author.created", "author.updated":
                logger.info("Received author with ID and name: {}", authorDTO.getAuthorID());
                if(authorRepository.findByAuthorID(authorDTO.getAuthorID()).isEmpty()) {
                    AuthorS author = new AuthorS(authorDTO.getName(), authorDTO.getBiography());
                    author.setAuthorID(authorDTO.getAuthorID());
                    logger.info("About to save author");
                    authorRepository.save(author);
                    logger.info("Author saved with ID: {}", author.getAuthorID());
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }

    @RabbitListener(queues = "reader.queue")
    public void notifyReader(ReaderDTO readerDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "reader.created", "reader.updated":
                logger.info("Received reader with ID and name: {}", readerDTO.getReaderID());
                if(readerRepository.findByReaderID(readerDTO.getReaderID()).isEmpty()) {
                    ReaderS r = new ReaderS(readerDTO.getName(), "x", readerDTO.getEmail(),
                            readerDTO.getBirthdate(), readerDTO.getPhoneNumber(), true);
                    r.setReaderID(readerDTO.getReaderID());
                    logger.info("About to save reader");
                    readerRepository.save(r);
                    logger.info("Reader saved with ID: {}", r.getReaderID());
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }
}
