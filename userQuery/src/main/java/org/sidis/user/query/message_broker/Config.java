package org.sidis.user.query.message_broker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class Config {

    @Bean
    public FanoutExchange fanoutUser() {
        return new FanoutExchange("user.fanout");
    }

    @Bean
    public FanoutExchange fanoutReader() {
        return new FanoutExchange("reader.fanout");
    }

    @Bean
    public Queue userQueue(){return new AnonymousQueue();}

    @Bean
    public Queue readerQueue(){return new AnonymousQueue();}

    @Bean
    public Binding userBinding() {return BindingBuilder.bind(userQueue()).to(fanoutUser());}

    @Bean
    public Binding readerBinding() {return BindingBuilder.bind(readerQueue()).to(fanoutReader());}

    @Bean
    public MessageConverter converter(){
        ObjectMapper dateMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return new Jackson2JsonMessageConverter(dateMapper);
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter());
        return rabbitTemplate;
    }

}
