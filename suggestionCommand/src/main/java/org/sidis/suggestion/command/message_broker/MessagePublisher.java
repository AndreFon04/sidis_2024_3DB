package org.sidis.suggestion.command.message_broker;

import org.sidis.suggestion.command.dto.SuggestionDTO;
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
        SuggestionDTO suggestionDTO = new SuggestionDTO(suggestion.getSuggestionID(), suggestion.getBookISBN(),
                suggestion.getBookTitle(), suggestion.getBookAuthorName(),
                suggestion.getReaderID(), suggestion.getNotes(), suggestion.getState());
        template.convertAndSend(fanoutSuggestion.getName(), "suggestion.created", suggestionDTO);
        logger.info("Sent suggestion.created --> ");
    }

    public void publishSuggestionUpdated(Suggestion suggestion) {
        SuggestionDTO suggestionDTO = new SuggestionDTO(suggestion.getSuggestionID(), suggestion.getBookISBN(),
                suggestion.getBookTitle(), suggestion.getBookAuthorName(),
                suggestion.getReaderID(), suggestion.getNotes(), suggestion.getState());
        template.convertAndSend(fanoutSuggestion.getName(), "suggestion.updated", suggestionDTO);
        logger.info("Sent suggestion.updated --> ");
    }
}
