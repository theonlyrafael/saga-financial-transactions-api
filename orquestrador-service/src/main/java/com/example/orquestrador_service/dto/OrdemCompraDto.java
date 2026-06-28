package com.example.orquestrador_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class OrdemCompraDto {

    // id da ordem de compra gerado pelo conta-service, que será usado para rastrear a transação 
    private UUID id;
    private String cpfCliente;
    private BigDecimal valor;

    public OrdemCompraDto() {
    }

    public OrdemCompraDto(UUID id, String cpfCliente, BigDecimal valor) {
        this.id = id;
        this.cpfCliente = cpfCliente;
        this.valor = valor;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}