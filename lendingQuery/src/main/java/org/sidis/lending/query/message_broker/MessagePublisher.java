package org.sidis.lending.query.message_broker;

import org.sidis.lending.query.model.Lending;
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
    private FanoutExchange fanoutLending;

    @Autowired
    private FanoutExchange fanoutBook;


    public void publishLendingCreated(Lending lending) {
        template.convertAndSend(fanoutLending.getName(), "lending.created", lending);
        logger.info("Sent lending.created --> ");
    }

    public void publishAuthorUpdated(Lending lending) {
        template.convertAndSend(fanoutLending.getName(), "lending.updated", lending);
        logger.info("Sent lending.updated --> ");
    }
}
