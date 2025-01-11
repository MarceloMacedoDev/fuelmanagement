package com.example.abastecimento_api.web.dto;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class AbastecimentoDTOTest {

    @Test
    void testAbastecimentoDTO() {
        // Testando o construtor sem parâmetros
 

        // Testando o construtor com parâmetros
        LocalDateTime dataHora = LocalDateTime.now();
        AbastecimentoDTO abastecimento2 = new AbastecimentoDTO(1L, "ABC-1234", 10000, dataHora, 150.50);
        assertEquals(1L, abastecimento2.getId(), "ID should be 1");
        assertEquals("ABC-1234", abastecimento2.getPlaca(), "Placa should be ABC-1234");
        assertEquals(10000, abastecimento2.getQuilometragem(), "Quilometragem should be 10000");
        assertEquals(dataHora, abastecimento2.getDataHora(), "DataHora should be " + dataHora);
        assertEquals(150.50, abastecimento2.getValorTotal(), "ValorTotal should be 150.50");

        // Testando getters e setters
        AbastecimentoDTO abastecimento3 = new AbastecimentoDTO();
        abastecimento3.setId(2L);
        abastecimento3.setPlaca("XYZ-9876");
        abastecimento3.setQuilometragem(25000);
        LocalDateTime dataHora2 = LocalDateTime.now();
        abastecimento3.setDataHora(dataHora2);
        abastecimento3.setValorTotal(300.00);

        assertEquals(2L, abastecimento3.getId(), "ID should be 2");
        assertEquals("XYZ-9876", abastecimento3.getPlaca(), "Placa should be XYZ-9876");
        assertEquals(25000, abastecimento3.getQuilometragem(), "Quilometragem should be 25000");
        assertEquals(dataHora2, abastecimento3.getDataHora(), "DataHora should be " + dataHora2);
        assertEquals(300.00, abastecimento3.getValorTotal(), "ValorTotal should be 300.00");
    }
}