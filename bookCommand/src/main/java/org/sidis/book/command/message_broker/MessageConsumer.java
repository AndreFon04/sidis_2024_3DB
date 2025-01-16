package org.sidis.book.command.message_broker;

import org.sidis.book.command.model.*;
import org.sidis.book.command.repositories.AuthorRepository;
import org.sidis.book.command.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import org.sidis.book.command.repositories.GenreRepository;
import org.sidis.book.command.service.AuthorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final AuthorRepository repository;
    private final BookRepository bookRepository;
    private final GenreRepository genreRepository;
    private final MessagePublisher messagePublisher;

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
                    Book book = new Book(bookDTO.getIsbn(), bookDTO.getTitle(), genre, bookDTO.getDescription(), authorList, null, 1); // -1 - Cancelled | 0 - Suggested | 1 - In library
                    book.setBookID(bookDTO.getBookId());
                    bookRepository.save(book);
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }

    @RabbitListener(queues = "#{suggestionQueue.name}")
    public void notifyS(SuggestionDTO suggestionDTO, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "suggestion.created":
                logger.info("Received suggestion with id: {} {}", suggestionDTO.getSuggestionID(), suggestionDTO.getBookISBN());

                Optional<Book> optionalBook = bookRepository.findByIsbn(suggestionDTO.getBookISBN());
                if (optionalBook.isEmpty()) { // book does not exist in the database -> try to create book as SUGGESTED
                    Genre genre = genreRepository.findByInterest("Drama");
                    Book book = new Book(suggestionDTO.getBookISBN(), suggestionDTO.getBookTitle(), genre, "", new ArrayList<Author>(), null, 0); // -1 - Cancelled | 0 - Suggested | 1 - In library
                    try {
                        bookRepository.save(book);
                        messagePublisher.publishSuggestedBookCreated(suggestionDTO);
                    } catch (Exception e) {  // creation of book in db failed, eg. invalid isbn, or other reason
                        messagePublisher.publishSuggestedBookCreationFailed(suggestionDTO);
                    }
                } else if (optionalBook.get().getBookStatus() == 1) {  // book already exists in library
                    messagePublisher.publishSuggestedBookAlreadyAcquired(suggestionDTO);
                } else if (optionalBook.get().getBookStatus() == 0) {  // book already SUGGESTED
                    messagePublisher.publishSuggestedBookAlreadySuggested(suggestionDTO);
                } else {                                                // other situations
                    messagePublisher.publishSuggestedBookCreationFailed(suggestionDTO);
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }




}
