package com.example.orquestrador_service.controller;

import com.example.orquestrador_service.domain.TransacaoSaga;
import com.example.orquestrador_service.service.OrquestradorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/sagas")
public class TransacaoSagaController {

    private final OrquestradorService service;

    // injeta o serviço de negócios
    public TransacaoSagaController(OrquestradorService service) {
        this.service = service;
    }

    // expõe o endpoint get para consulta do status da transação financeira
    @GetMapping("/{ordemId}")
    public ResponseEntity<TransacaoSaga> consultarStatusSaga(@PathVariable UUID ordemId) {
        Optional<TransacaoSaga> transacao = service.consultarStatus(ordemId);
        
        // retorna 200 ok com os dados ou 404 not found se não existir
        return transacao.map(ResponseEntity::ok)
                        .orElseGet(() -> ResponseEntity.notFound().build());
    }
}