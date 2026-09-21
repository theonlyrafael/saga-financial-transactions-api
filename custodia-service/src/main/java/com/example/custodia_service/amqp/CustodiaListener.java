package com.example.custodia_service.amqp;

import com.example.custodia_service.config.RabbitMQConfig;
import com.example.custodia_service.domain.AtivoCustodia;
import com.example.custodia_service.dto.OrdemCompraDto;
import com.example.custodia_service.dto.ResultadoSagaDto;
import com.example.custodia_service.repository.AtivoCustodiaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class CustodiaListener {

    private final AtivoCustodiaRepository repository;
    private final RabbitTemplate rabbitTemplate;

    // injeção de dependência via construtor
    public CustodiaListener(AtivoCustodiaRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    // ouvinte travado na fila exata que o conta-service configurou
    @RabbitListener(queues = "custodia.ordem.fila")
    public void processarOrdem(OrdemCompraDto ordemDto) {

        System.out.println("[Custodia-Service] SUCESSO: Ordem interceptada na fila 'custodia.ordem.fila'!");
        System.out.println("[Custodia-Service] Dados recebidos -> CPF: " + ordemDto.getCpfCliente() + " | Valor: "
                + ordemDto.getValor());

        // converte o dto recebido para a entidade de custódia
        AtivoCustodia ativo = new AtivoCustodia();
        ativo.setOrdemId(ordemDto.getId());
        ativo.setCpfCliente(ordemDto.getCpfCliente());
        ativo.setValor(ordemDto.getValor());
        ativo.setStatusCustodia("CUSTODIADO");

        // persiste a nova custódia no banco h2 isolado
        repository.save(ativo);

        System.out.println("[Custodia-Service] Ativo salvo com sucesso no banco H2.");

        // avisa o orquestrador-service que esta etapa da saga foi concluída com
        // sucesso,
        // para que o status da transação deixe de ficar parado em "INICIADA"
        ResultadoSagaDto resultado = new ResultadoSagaDto(ordemDto.getId(), "CONCLUIDA");
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_RESULTADO, RabbitMQConfig.ROUTING_KEY_RESULTADO,
                resultado);

        System.out.println("[Custodia-Service] Evento de sucesso publicado para a saga " + ordemDto.getId());
    }
}