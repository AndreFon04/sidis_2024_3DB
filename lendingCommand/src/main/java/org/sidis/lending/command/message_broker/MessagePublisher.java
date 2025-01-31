package org.sidis.lending.command.message_broker;

import org.sidis.lending.command.exceptions.NotFoundException;
import org.sidis.lending.command.model.Lending;
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


    public void publishLendingCreated(Lending lending) {
        template.convertAndSend(fanoutLending.getName(), "lending.created", lending);
        logger.info("Sent lending.created --> ");
    }

    public void publishLendingUpdated(Lending lending) {
        template.convertAndSend(fanoutLending.getName(), "lending.updated", lending);
        logger.info("Sent lending.updated --> ");
    }

//    public String askBookISBNbyID(Long bookID){
//        logger.info("Sending book query: " + bookID + " --> ");
//
//        String isbn = (String) template.convertSendAndReceive("book.query.queue", bookID);
//        if (isbn == null) {throw new NotFoundException("Book not found");}
//
//        logger.info("<-- Received book ISBN: " + isbn);
//        return isbn;
//    }
//
//    public boolean askReaderValidbyReaderID(String readerID){
//        return true;
//    }

}
