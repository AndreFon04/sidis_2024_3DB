package org.sidis.suggestion.query.message_broker;

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
    public FanoutExchange fanoutSuggestion() {
        return new FanoutExchange("suggestion.fanout");
    }

    @Bean
    public FanoutExchange fanoutBook() {
        return new FanoutExchange("book.fanout");
    }

    @Bean
    public FanoutExchange fanoutAuthor() {
        return new FanoutExchange("author.fanout");
    }

    @Bean
    public FanoutExchange fanoutReader() {
        return new FanoutExchange("reader.fanout");
    }


    @Bean
    public Queue suggestionQueue(){return new AnonymousQueue();}

    @Bean
    public Queue bookQueue(){return new AnonymousQueue();}

    @Bean
    public Queue authorQueue(){return new AnonymousQueue();}

    @Bean
    public Queue readerQueue(){return new AnonymousQueue();}


    @Bean
    public Binding suggestionBinding() {return BindingBuilder.bind(suggestionQueue()).to(fanoutSuggestion());}

    @Bean
    public Binding bookBinding() {return BindingBuilder.bind(bookQueue()).to(fanoutBook());}

    @Bean
    public Binding authorBinding() {return BindingBuilder.bind(authorQueue()).to(fanoutAuthor());}

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
