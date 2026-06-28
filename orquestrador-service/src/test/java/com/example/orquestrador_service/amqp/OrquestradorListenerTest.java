package com.example.orquestrador_service.amqp;

import com.example.orquestrador_service.config.RabbitMQConfig;
import com.example.orquestrador_service.domain.TransacaoSaga;
import com.example.orquestrador_service.dto.OrdemCompraDto;
import com.example.orquestrador_service.repository.TransacaoSagaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OrquestradorListenerTest {

    // simula o banco de dados
    @Mock
    private TransacaoSagaRepository repository;

    // simula a rede do rabbitmq
    @Mock
    private RabbitTemplate rabbitTemplate;

    // injeta os mocks falsos dentro do nosso listener real
    @InjectMocks
    private OrquestradorListener listener;

    @Test
    public void deveProcessarOrdemERotearParaCustodia() {
        // Passo 1: Cenário
        UUID ordemId = UUID.randomUUID();
        OrdemCompraDto dto = new OrdemCompraDto(ordemId, "12345678900", new BigDecimal("250.00"));

        // Passo 2: Ação
        listener.processarNovaOrdem(dto);

        // Passo 3: Validação
        // captura a entidade que o listener tentou salvar para inspecionar se os valores estão corretos
        ArgumentCaptor<TransacaoSaga> sagaCaptor = ArgumentCaptor.forClass(TransacaoSaga.class);
        verify(repository).save(sagaCaptor.capture());

        TransacaoSaga sagaSalva = sagaCaptor.getValue();
        assertThat(sagaSalva.getOrdemId()).isEqualTo(ordemId);
        assertThat(sagaSalva.getStatusSaga()).isEqualTo("INICIADA");

        // garante que a mensagem foi roteada com a exchange e a routing key exatas da custódia
        verify(rabbitTemplate).convertAndSend(RabbitMQConfig.EXCHANGE_CUSTODIA, RabbitMQConfig.ROUTING_KEY_CUSTODIA, dto);
    }
}