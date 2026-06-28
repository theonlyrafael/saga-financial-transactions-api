package com.example.orquestrador_service.repository;

import com.example.orquestrador_service.domain.TransacaoSaga;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TransacaoSagaRepository extends JpaRepository<TransacaoSaga, UUID> {
}