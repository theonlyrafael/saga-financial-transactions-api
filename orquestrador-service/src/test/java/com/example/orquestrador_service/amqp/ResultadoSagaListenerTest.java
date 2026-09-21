package com.example.orquestrador_service.amqp;

import com.example.orquestrador_service.domain.TransacaoSaga;
import com.example.orquestrador_service.dto.ResultadoSagaDto;
import com.example.orquestrador_service.repository.TransacaoSagaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ResultadoSagaListenerTest {

    // simula o banco de dados
    @Mock
    private TransacaoSagaRepository repository;

    // injeta o mock falso dentro do nosso listener real
    @InjectMocks
    private ResultadoSagaListener listener;

    @Test
    public void deveConcluirSagaQuandoResultadoDeSucessoChega() {
        // Passo 1: Cenário -- uma saga já registrada, ainda "INICIADA"
        UUID ordemId = UUID.randomUUID();
        TransacaoSaga saga = new TransacaoSaga(ordemId, "12345678900", new BigDecimal("250.00"));
        when(repository.findByOrdemId(ordemId)).thenReturn(Optional.of(saga));

        ResultadoSagaDto resultado = new ResultadoSagaDto(ordemId, "CONCLUIDA");

        // Passo 2: Ação
        listener.processarResultado(resultado);

        // Passo 3: Validação -- o status da saga deve ter sido atualizado e persistido
        ArgumentCaptor<TransacaoSaga> sagaCaptor = ArgumentCaptor.forClass(TransacaoSaga.class);
        verify(repository).save(sagaCaptor.capture());

        assertThat(sagaCaptor.getValue().getStatusSaga()).isEqualTo("CONCLUIDA");
        assertThat(sagaCaptor.getValue().getOrdemId()).isEqualTo(ordemId);
    }

    @Test
    public void naoDeveFalharQuandoSagaNaoEhEncontrada() {
        // Passo 1: Cenário -- nenhuma saga cadastrada para esse ordemId
        UUID ordemId = UUID.randomUUID();
        when(repository.findByOrdemId(ordemId)).thenReturn(Optional.empty());

        ResultadoSagaDto resultado = new ResultadoSagaDto(ordemId, "CONCLUIDA");

        // Passo 2: Ação
        listener.processarResultado(resultado);

        // Passo 3: Validação -- nada deve ser salvo, pois não há saga para atualizar
        verify(repository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}