package com.example.conta_service;

import com.example.conta_service.domain.OrdemCompra;
import com.example.conta_service.repository.OrdemCompraRepository;
import com.example.conta_service.service.OrdemCompraPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// sobe a aplicação inteira com banco de dados real (h2) configurado
@SpringBootTest
@AutoConfigureMockMvc
public class OrdemCompraIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrdemCompraRepository repository;

    // intercepta a saída para o rabbitmq para evitar erros de conexão no teste
    @MockitoBean
    private OrdemCompraPublisher publisher;

    // limpa o banco de dados antes de cada teste para garantir isolamento
    @BeforeEach
    public void prepararBanco() {
        repository.deleteAll();
    }

    @Test
    public void deveReceberSalvarEEnviarOrdemNoFluxoCompleto() throws Exception {
        // arrange (preparação do json)
        String jsonRequisicao = """
                {
                    "cpfCliente": "12345678900",
                    "valor": 1050.00
                }
                """;

        // act (acao simulando o envio http)
        mockMvc.perform(post("/api/ordens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequisicao))
                .andExpect(status().isOk());

        // assert (verificacao no banco de dados real)
        List<OrdemCompra> ordensSalvas = repository.findAll();
        
        assertFalse(ordensSalvas.isEmpty()); // garante que o banco nao esta vazio
        assertEquals(1, ordensSalvas.size()); // garante que apenas uma ordem foi salva
        assertEquals("12345678900", ordensSalvas.get(0).getCpfCliente());
        assertEquals("PENDENTE", ordensSalvas.get(0).getStatus());

        // assert (verificacao na saída da mensageria)
        verify(publisher).enviarOrdem(any(OrdemCompra.class));
    }
}
