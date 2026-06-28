package com.example.orquestrador_service.service;

import com.example.orquestrador_service.domain.TransacaoSaga;
import com.example.orquestrador_service.repository.TransacaoSagaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrquestradorServiceTest {

    // simula a interface do banco de dados
    @Mock
    private TransacaoSagaRepository repository;

    // injeta o falso repositório dentro do nosso serviço real
    @InjectMocks
    private OrquestradorService service;

    @Test
    public void deveRetornarTransacaoQuandoIdExistir() {
        // cenário
        UUID ordemId = UUID.randomUUID();
        TransacaoSaga transacao = new TransacaoSaga(ordemId, "12345678900", new BigDecimal("150.00"));
        
        // ensina o mock a retornar a transação quando o método for chamado
        when(repository.findByOrdemId(ordemId)).thenReturn(Optional.of(transacao));

        // ação
        Optional<TransacaoSaga> resultado = service.consultarStatus(ordemId);

        // validação
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getOrdemId()).isEqualTo(ordemId);
    }

    @Test
    public void deveRetornarVazioQuandoIdNaoExistir() {
        // cenário
        UUID ordemId = UUID.randomUUID();
        
        // ensina o mock a retornar vazio simulando que não achou no banco
        when(repository.findByOrdemId(ordemId)).thenReturn(Optional.empty());

        // ação
        Optional<TransacaoSaga> resultado = service.consultarStatus(ordemId);

        // validação
        assertThat(resultado).isEmpty();
    }
}