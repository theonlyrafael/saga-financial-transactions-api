package com.example.orquestrador_service.amqp;

import com.example.orquestrador_service.domain.TransacaoSaga;
import com.example.orquestrador_service.dto.OrdemCompraDto;
import com.example.orquestrador_service.repository.TransacaoSagaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrquestradorListener {

    private final TransacaoSagaRepository repository;
    private final RabbitTemplate rabbitTemplate;

    // injeta o repositório e o template do rabbitmq via construtor (melhor prática
    // do spring)
    public OrquestradorListener(TransacaoSagaRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // escuta a mesma fila onde o conta-service joga as ordens recém-criadas
    @RabbitListener(queues = "ordem.compra.fila")
    public void processarNovaOrdem(OrdemCompraDto dto) {

        // cria a ficha de rastreio da saga no banco de dados do orquestrador
        TransacaoSaga saga = new TransacaoSaga(dto.getId(), dto.getCpfCliente(), dto.getValor());
        repository.save(saga);

        System.out.println("Orquestrador interceptou a ordem de ID: " + dto.getId());

        // roteia a ordem para a fila exclusiva do custodia-service
        rabbitTemplate.convertAndSend("custodia.fila", dto);
    }
}