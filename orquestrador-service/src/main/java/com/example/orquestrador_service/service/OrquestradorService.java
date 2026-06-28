package com.example.orquestrador_service.service;

import com.example.orquestrador_service.domain.TransacaoSaga;
import com.example.orquestrador_service.repository.TransacaoSagaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrquestradorService {

    private final TransacaoSagaRepository repository;

    // injeta o repositório via construtor
    public OrquestradorService(TransacaoSagaRepository repository) {
        this.repository = repository;
    }

    // recupera a saga ou retorna vazio para o controller lidar com o erro
    public Optional<TransacaoSaga> consultarStatus(UUID ordemId) {
        return repository.findByOrdemId(ordemId);
    }
}