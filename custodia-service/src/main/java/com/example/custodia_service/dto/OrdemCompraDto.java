package com.example.custodia_service.dto;

import java.math.BigDecimal;
import java.util.UUID;

public class OrdemCompraDto {

    // id da ordem original, gerado no conta-service e repassado pelo orquestrador;
    // é o que permite ao custodia-service informar de volta a qual saga o resultado pertence
    private UUID id;
    private String cpfCliente;
    private BigDecimal valor;
    private String status;

    public OrdemCompraDto() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}