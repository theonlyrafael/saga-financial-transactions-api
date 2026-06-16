package com.example.conta_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // nomes da fila, exchange e routing key
    public static final String FILA_ORDEM_COMPRA = "ordem.compra.fila";
    public static final String EXCHANGE_ORDEM_COMPRA = "ordem.compra.exchange";
    public static final String ROUTING_KEY_ORDEM_COMPRA = "ordem.compra.routing.key";

    // 1. Cria a fila
    @Bean
    public Queue ordemCompraQueue() {
        return new Queue(FILA_ORDEM_COMPRA, true); // fila durável, não some se o RabbitMQ reiniciar
    }

    // 2. Cria a exchange do tipo Direct
    @Bean
    public DirectExchange ordemCompraExchange() {
        return new DirectExchange(EXCHANGE_ORDEM_COMPRA);
    }

    // 3. Faz o binding entre a fila e a exchange usando a routing key
    @Bean
    public Binding ordemCompraBinding(Queue ordemCompraQueue, DirectExchange ordemCompraExchange) {
        return BindingBuilder.bind(ordemCompraQueue).to(ordemCompraExchange).with(ROUTING_KEY_ORDEM_COMPRA);
    }
}