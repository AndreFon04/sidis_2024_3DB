package org.sidis.lending.command.message_broker;

import lombok.RequiredArgsConstructor;
import org.sidis.lending.command.model.Lending;
import org.sidis.lending.command.repositories.LendingRepository;
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
    private final LendingRepository lendingRepository;

    @RabbitListener(queues = "lending.queue")
    public void notify(Lending lending, @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String event) {
        logger.info("<-- Received {}", event);

        switch (event) {
            case "lending.created", "lending.updated":
                logger.info("Received lending with id: {}", lending.getLendingID());
                if(lendingRepository.findByLendingID(lending.getLendingID()).isEmpty()) {
                    // restart static internal ID
                    logger.info("About to save lending");
                    Lending l = new Lending(lending.getBookID(), lending.getReaderID(), lending.getStartDate(),
                            null, lending.getExpectedReturnDate(), false, 0);
                    l.updateOverdueStatus();
                    lendingRepository.save(l);
                    lendingRepository.save(lending);
                    logger.info("Saved lending");
                }
                break;

            default:
                logger.warn("/!\\ Unhandled event type: {}", event);
        }
    }
}
