package com.example.custodia_service.amqp;

import com.example.custodia_service.config.RabbitMQConfig;
import com.example.custodia_service.domain.AtivoCustodia;
import com.example.custodia_service.dto.OrdemCompraDto;
import com.example.custodia_service.dto.ResultadoSagaDto;
import com.example.custodia_service.repository.AtivoCustodiaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CustodiaListenerTest {

    @Mock
    private AtivoCustodiaRepository repository;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private CustodiaListener listener;

    @Test
    void deveProcessarOrdemESalvarCustodiaComSucesso() {
        UUID ordemId = UUID.randomUUID();

        OrdemCompraDto dto = new OrdemCompraDto();
        dto.setId(ordemId);
        dto.setCpfCliente("12345678900");
        dto.setValor(new BigDecimal("1500.50"));

        // executa o método alvo simulando que a mensagem chegou
        listener.processarOrdem(dto);

        // captura o objeto exato que foi repassado para o método save do repositório
        ArgumentCaptor<AtivoCustodia> captor = ArgumentCaptor.forClass(AtivoCustodia.class);
        verify(repository).save(captor.capture());

        AtivoCustodia ativoSalvo = captor.getValue();

        assertEquals(ordemId, ativoSalvo.getOrdemId());
        assertEquals("12345678900", ativoSalvo.getCpfCliente());
        assertEquals(new BigDecimal("1500.50"), ativoSalvo.getValor());
        assertEquals("CUSTODIADO", ativoSalvo.getStatusCustodia());
    }

    @Test
    void devePublicarEventoDeSucessoAposCustodiar() {
        UUID ordemId = UUID.randomUUID();

        OrdemCompraDto dto = new OrdemCompraDto();
        dto.setId(ordemId);
        dto.setCpfCliente("98765432100");
        dto.setValor(new BigDecimal("800.00"));

        listener.processarOrdem(dto);

        // confirma que o resultado foi publicado no exchange/routing key corretos,
        // com o mesmo ordemId recebido e status "CONCLUIDA"
        ArgumentCaptor<ResultadoSagaDto> captor = ArgumentCaptor.forClass(ResultadoSagaDto.class);
        verify(rabbitTemplate).convertAndSend(
                org.mockito.ArgumentMatchers.eq(RabbitMQConfig.EXCHANGE_RESULTADO),
                org.mockito.ArgumentMatchers.eq(RabbitMQConfig.ROUTING_KEY_RESULTADO),
                captor.capture());

        ResultadoSagaDto resultado = captor.getValue();
        assertEquals(ordemId, resultado.getOrdemId());
        assertEquals("CONCLUIDA", resultado.getStatus());
    }
}