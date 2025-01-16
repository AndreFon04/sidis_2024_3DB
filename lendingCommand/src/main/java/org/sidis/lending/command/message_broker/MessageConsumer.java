package org.sidis.lending.command.message_broker;

import lombok.RequiredArgsConstructor;
import org.sidis.lending.command.dto.AuthorDTO;
import org.sidis.lending.command.dto.BookDTO;
import org.sidis.lending.command.dto.BookResponse;
import org.sidis.lending.command.dto.ReaderDTO;
import org.sidis.lending.command.model.AuthorL;
import org.sidis.lending.command.model.BookL;
import org.sidis.lending.command.model.Lending;
import org.sidis.lending.command.model.ReaderL;
import org.sidis.lending.command.repositories.AuthorRepository;
import org.sidis.lending.command.repositories.BookRepository;
import org.sidis.lending.command.repositories.LendingRepository;
import org.sidis.lending.command.repositories.ReaderRepository;
import org.sidis.lending.command.service.AuthorService;
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
    private final LendingRepository lendingRepository;
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final ReaderRepository readerRepository;

    @RabbitListener(queues = "#{lendingQueue.name}")
    public void notify(Lending lending, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "lending.created", "lending.updated":
                logger.info("Received lending with id: {}", lending.getLendingID());
                if(lendingRepository.findByLendingID(lending.getLendingID()).isEmpty()) {
                    // restart static internal ID
                    logger.info("About to save lending");
                    Lending l = new Lending(lending.getBookID(), lending.getReaderID(), lending.getStartDate(),
                            null, lending.getExpectedReturnDate(), false, 0);
                    l.updateOverdueStatus();
                    lendingRepository.save(l);
                    lendingRepository.save(lending);
                    logger.info("Saved lending");
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }

    @RabbitListener(queues = "#{bookQueue.name}")
    public void notifyBook(BookDTO bookDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "book.created", "book.updated":
                logger.info("Received book with isbn: {}", bookDTO.getIsbn());
                if(bookRepository.findByIsbn(bookDTO.getIsbn()).isEmpty()) {
                    BookL b = new BookL(bookDTO.getIsbn(), bookDTO.getTitle(), bookDTO.getGenre(), bookDTO.getDescription(), bookDTO.getAuthor());
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

    @RabbitListener(queues = "#{authorQueue.name}")
    public void notifyAuthor(AuthorDTO authorDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "author.created", "author.updated":
                logger.info("Received author with ID and name: {}", authorDTO.getAuthorID());
                if(authorRepository.findByAuthorID(authorDTO.getAuthorID()).isEmpty()) {
                    AuthorL author = new AuthorL(authorDTO.getName(), authorDTO.getBiography());
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

    @RabbitListener(queues = "#{readerQueue.name}")
    public void notifyReader(ReaderDTO readerDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "reader.created", "reader.updated":
                logger.info("Received reader with ID and name: {}", readerDTO.getReaderID());
                if(readerRepository.findByReaderID(readerDTO.getReaderID()).isEmpty()) {
                    ReaderL r = new ReaderL(readerDTO.getName(), "x", readerDTO.getEmail(),
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
