
package com.example.abastecimento_api.domain.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

/**
 *
 * @author marcelo
 * 
 */
public class AbastecimentoTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testValidAbastecimento() {
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setPlaca("ABC-1234");
        abastecimento.setQuilometragem(100);
        abastecimento.setDataHora(LocalDateTime.now().minusDays(1));
        abastecimento.setValorTotal(50.0);

        var violations = validator.validate(abastecimento);
        assertEquals(0, violations.size());
    }

    @Test
    void testInvalidPlaca() {
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setPlaca("");
        abastecimento.setQuilometragem(100);
        abastecimento.setDataHora(LocalDateTime.now().minusDays(1));
        abastecimento.setValorTotal(50.0);

        var violations = validator.validate(abastecimento);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidQuilometragem() {
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setPlaca("ABC-1234");
        abastecimento.setQuilometragem(-1);
        abastecimento.setDataHora(LocalDateTime.now().minusDays(1));
        abastecimento.setValorTotal(50.0);

        var violations = validator.validate(abastecimento);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidDataHora() {
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setPlaca("ABC-1234");
        abastecimento.setQuilometragem(100);
        abastecimento.setDataHora(LocalDateTime.now().plusDays(1));
        abastecimento.setValorTotal(50.0);

        var violations = validator.validate(abastecimento);
        assertEquals(1, violations.size());
    }

    @Test
    void testInvalidValorTotal() {
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setPlaca("ABC-1234");
        abastecimento.setQuilometragem(100);
        abastecimento.setDataHora(LocalDateTime.now().minusDays(1));
        abastecimento.setValorTotal(-10.0);

        var violations = validator.validate(abastecimento);
        assertEquals(1, violations.size());
    }
    
    @Test
    void testAbastecimento() {
        // Testando o construtor sem argumentos
        Abastecimento abastecimentoVazio = new Abastecimento();
        assertNull(abastecimentoVazio.getId());
        assertNull(abastecimentoVazio.getPlaca());
        assertNull(abastecimentoVazio.getQuilometragem());
        assertNull(abastecimentoVazio.getDataHora());
        assertEquals(0.0, abastecimentoVazio.getValorTotal());

        // Testando o construtor com argumentos
        LocalDateTime dataHora = LocalDateTime.now().minusDays(1);
        Abastecimento abastecimentoCheio = new Abastecimento(1L, "ABC-1234", 10000, dataHora, 150.50);

        assertEquals(1L, abastecimentoCheio.getId());
        assertEquals("ABC-1234", abastecimentoCheio.getPlaca());
        assertEquals(10000, abastecimentoCheio.getQuilometragem());
        assertEquals(dataHora, abastecimentoCheio.getDataHora());
        assertEquals(150.50, abastecimentoCheio.getValorTotal());

        // Testando os setters
        abastecimentoCheio.setPlaca("XYZ-5678");
        abastecimentoCheio.setQuilometragem(12000);
        LocalDateTime novaDataHora = LocalDateTime.now().minusDays(2);
        abastecimentoCheio.setDataHora(novaDataHora);
        abastecimentoCheio.setValorTotal(200.00);

        abastecimentoCheio.setId(1L);
        assertEquals(1L, abastecimentoCheio.getId());

        assertEquals("XYZ-5678", abastecimentoCheio.getPlaca());
        assertEquals(12000, abastecimentoCheio.getQuilometragem());
        assertEquals(novaDataHora, abastecimentoCheio.getDataHora());
        assertEquals(200.00, abastecimentoCheio.getValorTotal());
    }
   

}
