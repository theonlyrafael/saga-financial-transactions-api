package com.example.orquestrador_service.repository;

import com.example.orquestrador_service.domain.TransacaoSaga;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TransacaoSagaRepositoryTest {

    // injeta o repositório a ser testado automaticamente pelo Spring
    @Autowired
    private TransacaoSagaRepository repository;

    @Test
    public void deveSalvarTransacaoSagaComSucesso() {
        // Passo 1: Cenário
        UUID ordemId = UUID.randomUUID();
        TransacaoSaga transacao = new TransacaoSaga(ordemId, "12345678900", new BigDecimal("250.00"));

        // Passo 2: Ação
        TransacaoSaga transacaoSalva = repository.save(transacao);

        // Passo 3: Validação
        // garante que o banco gerou o id do orquestrador e manteve a integridade dos dados
        assertThat(transacaoSalva.getId()).isNotNull();
        assertThat(transacaoSalva.getOrdemId()).isEqualTo(ordemId);
        assertThat(transacaoSalva.getCpfCliente()).isEqualTo("12345678900");
        assertThat(transacaoSalva.getValor()).isEqualTo(new BigDecimal("250.00"));
        assertThat(transacaoSalva.getStatusSaga()).isEqualTo("INICIADA");
        assertThat(transacaoSalva.getDataCriacao()).isNotNull();
    }
}