package org.sidis.book.command.message_broker;

import org.sidis.book.command.api.AuthorView;
import org.sidis.book.command.model.Author;
import org.sidis.book.command.model.AuthorDTO;
import org.sidis.book.command.model.Book;
import org.sidis.book.command.model.BookDTO;
import org.sidis.book.command.model.SuggestionDTO;
import org.sidis.book.command.repositories.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private FanoutExchange fanoutAuthor;

    @Autowired
    private FanoutExchange fanoutBook;
    @Autowired
    private FanoutExchange fanoutSuggestion;


    public void publishAuthorCreated(Author author) {
        AuthorDTO authorDTO = new AuthorDTO(author.getName(), author.getBiography(), author.getAuthorID());
        template.convertAndSend(fanoutAuthor.getName(), "author.created", authorDTO);
        logger.info("Sent author.created --> ");
    }

    public void publishAuthorUpdated(Author author) {
        AuthorDTO authorDTO = new AuthorDTO(author.getName(), author.getBiography(), author.getAuthorID());
        template.convertAndSend(fanoutAuthor.getName(), "author.updated", authorDTO);
        logger.info("Sent author.updated --> ");
    }


    public void publishBookCreated(Book book) {
        System.out.println(book.getTitle() + "|" + book.getGenre() + "|" + book.getDescription() + "|" + book.getIsbn() +
                "|" + book.getBookImage() + "|" + book.getAuthor());
        List<String> authors = new ArrayList<>();
        for (Author author : book.getAuthor()) {
            authors.add(author.getAuthorID());
        }
        BookDTO bookDTO = new BookDTO(book.getBookID(), book.getTitle(), book.getIsbn(), book.getDescription(),
                book.getGenre().getInterest(), authors, 1);
        template.convertAndSend(fanoutBook.getName(), "book.created", bookDTO);
        logger.info("Sent book.created --> " + fanoutBook.getName() + " / " + "book.created");
    }

    public void publishBookUpdated(Book book) {
        List<String> authors = new ArrayList<>();
        for (Author author : book.getAuthor()) {
            authors.add(author.getAuthorID());
        }
        BookDTO bookDTO = new BookDTO(book.getBookID(), book.getTitle(), book.getIsbn(), book.getDescription(),
                book.getGenre().getInterest(), authors, 1);
        template.convertAndSend(fanoutBook.getName(), "book.updated", bookDTO);
        logger.info("Sent book.updated --> ");
    }


    public void publishSuggestedBookCreated(SuggestionDTO suggestionDTO) {
        template.convertAndSend(fanoutSuggestion.getName(), "suggested.book.created", suggestionDTO);
        logger.info("Sent suggested.book.created --> ");
    }

    public void publishSuggestedBookAlreadyAcquired(SuggestionDTO suggestionDTO) {
        template.convertAndSend(fanoutSuggestion.getName(), "suggested.book.already.acquired", suggestionDTO);
        logger.info("Sent suggested.book.already.acquired --> ");
    }

    public void publishSuggestedBookAlreadySuggested(SuggestionDTO suggestionDTO) {
        template.convertAndSend(fanoutSuggestion.getName(), "suggested.book.already.suggested", suggestionDTO);
        logger.info("Sent suggested.book.already.suggested --> ");
    }

    public void publishSuggestedBookCreationFailed(SuggestionDTO suggestionDTO) {
        template.convertAndSend(fanoutSuggestion.getName(), "suggested.book.creation.failed", suggestionDTO);
        logger.info("Sent suggested.book.creation.failed --> ");
    }

}
