package com.example.orquestrador_service.repository;

import com.example.orquestrador_service.domain.TransacaoSaga;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

// interface que herda os métodos padrão do spring data jpa para salvar e buscar o histórico da saga
public interface TransacaoSagaRepository extends JpaRepository<TransacaoSaga, UUID> {

    // busca a transação específica utilizando o id da ordem original
    Optional<TransacaoSaga> findByOrdemId(UUID ordemId);
}