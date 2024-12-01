package org.sidis.book.command.message_broker;

import org.sidis.book.command.api.AuthorView;
import org.sidis.book.command.model.Author;
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
    private FanoutExchange fanout;

    public void publishAuthorCreated(Author author) {
        template.convertAndSend(fanout.getName(), "author.created", author);
        logger.info("Sent author.created --> ");
    }

    public void publishAuthorUpdated(Author author) {
        template.convertAndSend(fanout.getName(), "author.updated", author);
        logger.info("Sent author.updated --> ");
    }
}
