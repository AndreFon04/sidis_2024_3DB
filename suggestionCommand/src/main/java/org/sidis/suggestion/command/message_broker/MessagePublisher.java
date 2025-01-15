package org.sidis.suggestion.command.message_broker;

import org.sidis.suggestion.command.model.Suggestion;
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
    private FanoutExchange fanoutSuggestion;


    public void publishSuggestionCreated(Suggestion suggestion) {
        template.convertAndSend(fanoutSuggestion.getName(), "suggestion.created", suggestion);
        logger.info("Sent suggestion.created --> ");
    }

    public void publishSuggestionUpdated(Suggestion suggestion) {
        template.convertAndSend(fanoutSuggestion.getName(), "suggestion.updated", suggestion);
        logger.info("Sent suggestion.updated --> ");
    }
}
