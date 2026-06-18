package com.example.conta_service.repository;

import com.example.conta_service.domain.OrdemCompra;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// anotação que sobe apenas a camada de banco de dados e configura o h2 automaticamente
@DataJpaTest
public class OrdemCompraRepositoryTest {

    @Autowired
    private OrdemCompraRepository repository;

    @Test
    public void deveSalvarEBuscarOrdemDeCompraComSucesso() {
        // arrange (preparação)
        OrdemCompra novaOrdem = new OrdemCompra("12345678900", new BigDecimal("1050.00"));

        // act (ação)
        // salva a entidade no banco de dados e retorna o objeto com o id gerado
        OrdemCompra ordemSalva = repository.save(novaOrdem);

        // busca a ordem no banco utilizando o id que foi gerado na etapa anterior
        Optional<OrdemCompra> ordemBuscada = repository.findById(ordemSalva.getId());

        // assert (verificação)
        assertTrue(ordemBuscada.isPresent()); // garante que o registro foi encontrado
        assertNotNull(ordemSalva.getId()); // garante que o banco gerou um uuid
        assertEquals("12345678900", ordemBuscada.get().getCpfCliente());
        assertEquals(new BigDecimal("1050.00"), ordemBuscada.get().getValor());
        assertEquals("PENDENTE", ordemBuscada.get().getStatus());
    }
}
