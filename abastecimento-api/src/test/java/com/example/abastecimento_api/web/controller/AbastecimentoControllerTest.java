
package com.example.abastecimento_api.web.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.abastecimento_api.usecase.GerenciarAbastecimentoUseCase;
import com.example.abastecimento_api.web.dto.AbastecimentoDTO;

// Dummy classes for compilation 

@ExtendWith(MockitoExtension.class)
class AbastecimentoControllerTest {

    @Mock
    private GerenciarAbastecimentoUseCase abastecimentoService;

    @InjectMocks
    private AbastecimentoController abastecimentoController;

    @Test
    void listarAbastecimentos_semFiltro() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<AbastecimentoDTO> mockPage = new PageImpl<>(Collections.emptyList());
        when(abastecimentoService.listarAbastecimentos(null, pageable)).thenReturn(mockPage);

        ResponseEntity<Page<AbastecimentoDTO>> response = abastecimentoController.listarAbastecimentos(null, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPage, response.getBody());
        verify(abastecimentoService).listarAbastecimentos(null, pageable);
    }

    @Test
    void criarAbastecimento_sucesso() {
        AbastecimentoDTO abastecimentoDTO = new AbastecimentoDTO();
        AbastecimentoDTO abastecimentoDTOCriado = new AbastecimentoDTO();
        when(abastecimentoService.adicionarAbastecimento(abastecimentoDTO)).thenReturn(abastecimentoDTOCriado);

        ResponseEntity<AbastecimentoDTO> response = abastecimentoController.criarAbastecimento(abastecimentoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(abastecimentoDTOCriado, response.getBody());
        verify(abastecimentoService).adicionarAbastecimento(abastecimentoDTO);
    }

    @Test
    void deletarAbastecimento_sucesso() {
        Long id = 1L;
        doNothing().when(abastecimentoService).removerAbastecimento(id);

        ResponseEntity<Void> response = abastecimentoController.deletarAbastecimento(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(abastecimentoService).removerAbastecimento(id);
    }
}
