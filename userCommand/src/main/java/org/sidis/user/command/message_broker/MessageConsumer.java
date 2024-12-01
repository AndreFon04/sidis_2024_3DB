package org.sidis.user.command.message_broker;

import lombok.RequiredArgsConstructor;
import org.sidis.user.command.model.User;
import org.sidis.user.command.model.Reader;
import org.sidis.user.command.repositories.UserRepository;
import org.sidis.user.command.repositories.ReaderRepository;
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
    private final UserRepository userRepository;
    private final ReaderRepository readerRepository;

    @RabbitListener(queues = "#{userQueue.name}")
    public void notify(User user, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        if (event.equals("user.created")) {
            logger.info("Received user with id: {}", user.getId());
            if (userRepository.findById(user.getId()).isEmpty()) {
                // restart static internal ID
                userRepository.save(user);
                logger.info("User save reached");
            }
        } else {
            logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }

    @RabbitListener(queues = "#{readerQueue.name}")
    public void notifyB(Reader reader, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "reader.created", "reader.updated":
                logger.info("Received reader with id: {}", reader.getReaderID());
                if(readerRepository.findByReaderID(reader.getReaderID()).isEmpty()) {
                    reader.initCounter(reader.getReaderID());
                    readerRepository.save(reader);
                    logger.info("Reader save reached");
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }
}
