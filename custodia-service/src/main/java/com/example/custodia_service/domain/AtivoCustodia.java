package com.example.custodia_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class AtivoCustodia {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String cpfCliente;
    private BigDecimal valor;
    private String statusCustodia;

    public AtivoCustodia() {
    }

    public AtivoCustodia(String cpfCliente, BigDecimal valor, String statusCustodia) {
        this.cpfCliente = cpfCliente;
        this.valor = valor;
        this.statusCustodia = statusCustodia;
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

    public String getStatusCustodia() {
        return statusCustodia;
    }

    public void setStatusCustodia(String statusCustodia) {
        this.statusCustodia = statusCustodia;
    }
}