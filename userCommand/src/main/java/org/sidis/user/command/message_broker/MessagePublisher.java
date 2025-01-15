package org.sidis.user.command.message_broker;

import org.sidis.user.command.model.ReaderDTO;
import org.sidis.user.command.model.User;
import org.sidis.user.command.model.Reader;
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
    private FanoutExchange fanoutUser;

    @Autowired
    private FanoutExchange fanoutReader;


    public void publishUserCreated(User user) {
        template.convertAndSend(fanoutUser.getName(), "user.created", user);
        logger.info("Sent user.created --> ");
    }

    public void publishReaderCreated(ReaderDTO readerDTO) {
        template.convertAndSend(fanoutReader.getName(), "reader.created", readerDTO);
        logger.info("Sent reader.created --> ");
    }

    public void publishReaderUpdated(ReaderDTO readerDTO) {
        template.convertAndSend(fanoutReader.getName(), "reader.updated", readerDTO);
        logger.info("Sent reader.updated --> ");
    }
}
