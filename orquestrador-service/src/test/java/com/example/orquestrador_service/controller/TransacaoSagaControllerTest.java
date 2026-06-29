package com.example.orquestrador_service.controller;

import com.example.orquestrador_service.domain.TransacaoSaga;
import com.example.orquestrador_service.service.OrquestradorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransacaoSagaController.class)
public class TransacaoSagaControllerTest {

    // ferramenta do spring para simular requisições http
    @Autowired
    private MockMvc mockMvc;

    // mockbean substitui o componente real do serviço no contexto do spring
    @MockitoBean
    private OrquestradorService service;

    @Test
    public void deveRetornar200ETransacaoQuandoExistir() throws Exception {
        // cenário
        UUID ordemId = UUID.randomUUID();
        TransacaoSaga transacao = new TransacaoSaga(ordemId, "12345678900", new BigDecimal("150.00"));
        transacao.setId(UUID.randomUUID());
        
        when(service.consultarStatus(ordemId)).thenReturn(Optional.of(transacao));

        // ação e validação de corpo e status http
        mockMvc.perform(get("/api/sagas/" + ordemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cpfCliente").value("12345678900"))
                .andExpect(jsonPath("$.statusSaga").value("INICIADA"));
    }

    @Test
    public void deveRetornar404QuandoNaoExistir() throws Exception {
        // cenário
        UUID ordemId = UUID.randomUUID();
        when(service.consultarStatus(ordemId)).thenReturn(Optional.empty());

        // ação e validação de status http
        mockMvc.perform(get("/api/sagas/" + ordemId))
                .andExpect(status().isNotFound());
    }
}