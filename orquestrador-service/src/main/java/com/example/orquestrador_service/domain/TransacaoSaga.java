package com.example.orquestrador_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class TransacaoSaga {

    // id interno do orquestrador para gerenciar a máquina de estados
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // guarda o id exato gerado lá no conta-service para conseguirmos rastrear a
    // origem
    private UUID ordemId;

    private String cpfCliente;
    private BigDecimal valor;

    // registra em qual etapa da arquitetura a transação parou
    private String statusSaga;

    // carimbo de tempo para auditoria, marcando quando a transação entrou no
    // orquestrador
    private LocalDateTime dataCriacao;

    // construtor padrão, obrigatório para o hibernate funcionar
    public TransacaoSaga() {
    }

    public TransacaoSaga(UUID ordemId, String cpfCliente, BigDecimal valor) {
        this.ordemId = ordemId;
        this.cpfCliente = cpfCliente;
        this.valor = valor;
        this.statusSaga = "INICIADA";
        this.dataCriacao = LocalDateTime.now();
    }

    // getters e setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrdemId() {
        return ordemId;
    }

    public void setOrdemId(UUID ordemId) {
        this.ordemId = ordemId;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public void setCpfCliente(String cpfCliente) {
        this.cpfCliente = cpfCliente;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatusSaga() {
        return statusSaga;
    }

    public void setStatusSaga(String statusSaga) {
        this.statusSaga = statusSaga;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}