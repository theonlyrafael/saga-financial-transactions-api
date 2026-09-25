package com.example.custodia_service.config;

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

    // nomes da fila, exchange e routing key usados para devolver o resultado
    // do passo de custódia para o orquestrador-service
    public static final String FILA_RESULTADO = "resultado.saga.fila";
    public static final String EXCHANGE_RESULTADO = "resultado.saga.exchange";
    public static final String ROUTING_KEY_RESULTADO = "resultado.saga.routing.key";

    // 1. Cria a fila de resultado (o custodia-service é quem produz este evento,
    // então segue o mesmo padrão dos demais serviços: quem publica, declara)
    @Bean
    public Queue resultadoQueue() {
        return new Queue(FILA_RESULTADO, true);
    }

    // 2. Cria o exchange do tipo Direct
    @Bean
    public DirectExchange resultadoExchange() {
        return new DirectExchange(EXCHANGE_RESULTADO);
    }

    // 3. Faz o binding entre a fila e o exchange usando a routing key
    @Bean
    public Binding resultadoBinding(Queue resultadoQueue, DirectExchange resultadoExchange) {
        return BindingBuilder.bind(resultadoQueue).to(resultadoExchange).with(ROUTING_KEY_RESULTADO);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }

    // necessário para o CustodiaListener conseguir publicar o resultado de volta
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter);
        return template;
    }
}