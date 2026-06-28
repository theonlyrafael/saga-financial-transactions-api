package com.example.orquestrador_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String FILA_CUSTODIA = "custodia.ordem.fila";
    public static final String EXCHANGE_CUSTODIA = "custodia.ordem.exchange";
    public static final String ROUTING_KEY_CUSTODIA = "custodia.ordem.routing.key";

    // 1. Cria a fila exclusiva para o custodia-service escutar depois
    @Bean
    public Queue custodiaQueue() {
        return new Queue(FILA_CUSTODIA, true);
    }

    // 2. Cria o exchange do tipo direct para garantir o roteamento exato
    @Bean
    public DirectExchange custodiaExchange() {
        return new DirectExchange(EXCHANGE_CUSTODIA);
    }

    // 3. Faz o binding amarrando a fila ao exchange através da routing key
    @Bean
    public Binding custodiaBinding(Queue custodiaQueue, DirectExchange custodiaExchange) {
        return BindingBuilder.bind(custodiaQueue).to(custodiaExchange).with(ROUTING_KEY_CUSTODIA);
    }

    // 4. Mantém o conversor idêntico validado para o spring boot 4.1.0
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // 5. Mantém o template configurado para o envio do orquestrador
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}