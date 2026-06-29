package com.example.custodia_service.amqp;

import com.example.custodia_service.domain.AtivoCustodia;
import com.example.custodia_service.dto.OrdemCompraDto;
import com.example.custodia_service.repository.AtivoCustodiaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CustodiaListener {

    private final AtivoCustodiaRepository repository;

    // injeção de dependência via construtor
    public CustodiaListener(AtivoCustodiaRepository repository) {
        this.repository = repository;
    }

    // ouvinte travado na fila exata que o conta-service configurou
    @RabbitListener(queues = "custodia.ordem.fila")
    public void processarOrdem(OrdemCompraDto ordemDto) {

        System.out.println("[Custodia-Service] SUCESSO: Ordem interceptada na fila 'custodia.ordem.fila'!");
        System.out.println("[Custodia-Service] Dados recebidos -> CPF: " + ordemDto.getCpfCliente() + " | Valor: "
                + ordemDto.getValor());

        // converte o dto recebido para a entidade de custódia
        AtivoCustodia ativo = new AtivoCustodia();
        ativo.setCpfCliente(ordemDto.getCpfCliente());
        ativo.setValor(ordemDto.getValor());
        ativo.setStatusCustodia("CUSTODIADO");

        // persiste a nova custódia no banco h2 isolado
        repository.save(ativo);

        System.out.println("[Custodia-Service] Ativo salvo com sucesso no banco H2.");
    }
}
