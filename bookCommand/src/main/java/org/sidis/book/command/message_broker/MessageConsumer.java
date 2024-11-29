package org.sidis.book.command.message_broker;

import org.sidis.book.command.api.AuthorView;
import org.sidis.book.command.api.AuthorViewMapper;
import org.sidis.book.command.model.Author;
import org.sidis.book.command.repositories.AuthorRepository;
import org.sidis.book.command.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);
    private final AuthorRepository repository;
    private final AuthorViewMapper authorViewMapper;
    private final AuthorService authorService;

    @RabbitListener(queues = "#{authQueue.name}")
    public void notify(AuthorView authorView, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "author.created", "author.updated":
                logger.info("Received author with id: {}", authorView.getAuthorID());
                if(repository.existsById(authorView.getAuthorId())) {
                    repository.deleteById(authorView.getAuthorId());
                }
                Author author = authorViewMapper.toAuthorView1(authorView);
                repository.save(author);
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }
}
