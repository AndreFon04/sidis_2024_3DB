package org.sidis.book.command.message_broker;

import org.sidis.book.command.api.AuthorView;
import org.sidis.book.command.model.Author;
import org.sidis.book.command.model.Book;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {

    private static final Logger logger = LoggerFactory.getLogger(MessagePublisher.class);

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private FanoutExchange fanoutAuthor;

    @Autowired
    private FanoutExchange fanoutBook;


    public void publishAuthorCreated(Author author) {
        template.convertAndSend(fanoutAuthor.getName(), "author.created", author);
        logger.info("Sent author.created --> ");
    }

    public void publishAuthorUpdated(Author author) {
        template.convertAndSend(fanoutAuthor.getName(), "author.updated", author);
        logger.info("Sent author.updated --> ");
    }


    public void publishBookCreated(Book book) {
        System.out.println(book.getTitle() + "|" + book.getGenre() + "|" + book.getDescription() + "|" + book.getIsbn() +
                "|" + book.getBookImage() + "|" + book.getAuthor());
        template.convertAndSend(fanoutBook.getName(), "book.created", book);
        logger.info("Sent book.created --> ");
    }

    public void publishBookUpdated(Book book) {
        template.convertAndSend(fanoutBook.getName(), "book.updated", book);
        logger.info("Sent book.updated --> ");
    }
}
