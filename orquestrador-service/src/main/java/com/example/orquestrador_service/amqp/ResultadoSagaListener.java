package com.example.orquestrador_service.amqp;

import com.example.orquestrador_service.domain.TransacaoSaga;
import com.example.orquestrador_service.dto.ResultadoSagaDto;
import com.example.orquestrador_service.repository.TransacaoSagaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ResultadoSagaListener {

    private final TransacaoSagaRepository repository;

    // injeta o repositório via construtor
    public ResultadoSagaListener(TransacaoSagaRepository repository) {
        this.repository = repository;
    }

    // ouvinte travado na fila que o custodia-service declara e publica ao final do
    // seu processamento
    @RabbitListener(queues = "resultado.saga.fila")
    public void processarResultado(ResultadoSagaDto resultado) {

        Optional<TransacaoSaga> sagaOpt = repository.findByOrdemId(resultado.getOrdemId());

        if (sagaOpt.isEmpty()) {
            // saga não encontrada (mensagem duplicada, fora de ordem, etc.) -- não há o que
            // atualizar
            System.out.println("[Orquestrador-Service] Resultado recebido para saga desconhecida: "
                    + resultado.getOrdemId());
            return;
        }

        TransacaoSaga saga = sagaOpt.get();
        saga.setStatusSaga(resultado.getStatus());
        repository.save(saga);

        System.out.println("[Orquestrador-Service] Saga " + resultado.getOrdemId() + " atualizada para status: "
                + resultado.getStatus());
    }
}