package com.example.conta_service.controller;

import com.example.conta_service.domain.OrdemCompra;
import com.example.conta_service.service.OrdemCompraPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordens")
public class OrdemCompraController {

    private final OrdemCompraPublisher publisher;

    // injeção de dependência do nosso serviço
    public OrdemCompraController(OrdemCompraPublisher publisher) {
        this.publisher = publisher;
    }

    @PostMapping
    public ResponseEntity<String> criarOrdemCompra(@RequestBody OrdemCompra ordem) {
        // manda o serviço despachar a mensagem para o rabbitmq
        publisher.enviarOrdem(ordem);
        
        // retorna um HTTP 200 OK para quem fez a requisição
        return ResponseEntity.ok("Ordem de compra recebida e enfileirada com sucesso! ID: " + ordem.getId());
    }
}