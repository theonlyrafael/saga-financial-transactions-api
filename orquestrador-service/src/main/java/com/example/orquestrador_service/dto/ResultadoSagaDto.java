package com.example.orquestrador_service.dto;

import java.util.UUID;

// mensagem recebida do custodia-service informando o desfecho do passo de
// custódia para uma determinada saga
public class ResultadoSagaDto {

    private UUID ordemId;
    private String status;

    public ResultadoSagaDto() {
    }

    public ResultadoSagaDto(UUID ordemId, String status) {
        this.ordemId = ordemId;
        this.status = status;
    }

    public UUID getOrdemId() {
        return ordemId;
    }

    public void setOrdemId(UUID ordemId) {
        this.ordemId = ordemId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}