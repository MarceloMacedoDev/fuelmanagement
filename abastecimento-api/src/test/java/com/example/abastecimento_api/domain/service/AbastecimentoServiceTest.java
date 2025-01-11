package com.example.abastecimento_api.domain.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.abastecimento_api.domain.entity.Abastecimento;
import com.example.abastecimento_api.domain.exceptions.FuelingValidationException;
import com.example.abastecimento_api.infrastructure.repository.AbastecimentoRepository;

@ExtendWith(MockitoExtension.class)
public class AbastecimentoServiceTest {

    @Mock
    private AbastecimentoRepository abastecimentoRepository;

    @InjectMocks
    private AbastecimentoService abastecimentoService;

    @Test
    void testBuscarPorId() {
        Long id = 1L;
        Abastecimento abastecimento = new Abastecimento(); // Create a dummy Abastecimento object
        abastecimento.setId(id);
        when(abastecimentoRepository.findById(id)).thenReturn(Optional.of(abastecimento));
        Optional<Abastecimento> result = abastecimentoService.buscarPorId(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());

        assertThrows(FuelingValidationException.class, () -> abastecimentoService.buscarPorId(null));
    }

    @Test
    void testGetUltimoAbastecimentoPorPlaca() {
        String placa = "ABC-1234";
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setPlaca(placa);
        when(abastecimentoRepository.findTopByPlacaOrderByDataHoraDesc(placa)).thenReturn(abastecimento);
        Abastecimento result = abastecimentoService.getUltimoAbastecimentoPorPlaca(placa);
        assertEquals(placa, result.getPlaca());

        assertNull(abastecimentoService.getUltimoAbastecimentoPorPlaca(null)); // Testando caso nulo
        assertNull(abastecimentoService.getUltimoAbastecimentoPorPlaca("")); // Testando caso vazio

    }

    @Test
    void testListarPorPlaca() {
        String placa = "ABC-1234";
        Pageable pageable = PageRequest.of(0, 10);
        List<Abastecimento> abastecimentos = new ArrayList<>();
        Page<Abastecimento> page = new PageImpl<>(abastecimentos);
        when(abastecimentoRepository.findByPlaca(pageable, placa)).thenReturn(page);
        Page<Abastecimento> result = abastecimentoService.listarPorPlaca(pageable, placa);
        assertEquals(0, result.getTotalElements());

        assertThrows(FuelingValidationException.class, () -> abastecimentoService.listarPorPlaca(pageable, null));
        assertThrows(FuelingValidationException.class, () -> abastecimentoService.listarPorPlaca(pageable, ""));
    }

    @Test
    void testListarTodos() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Abastecimento> abastecimentos = new ArrayList<>();
        Page<Abastecimento> page = new PageImpl<>(abastecimentos);
        when(abastecimentoRepository.findAll(pageable)).thenReturn(page);
        Page<Abastecimento> result = abastecimentoService.listarTodos(pageable);
        assertEquals(0, result.getTotalElements());

    }

    @Test
    void testRemover() {
        Long id = 1L;
        abastecimentoService.remover(id);
        verify(abastecimentoRepository).deleteById(id);

        assertThrows(FuelingValidationException.class, () -> abastecimentoService.remover(null));
        assertThrows(FuelingValidationException.class, () -> abastecimentoService.remover(0L));
        assertThrows(FuelingValidationException.class, () -> abastecimentoService.remover(-1L));

    }

    @Test
    void testSalvar() {
        Abastecimento abastecimento = new Abastecimento();
        when(abastecimentoRepository.save(abastecimento)).thenReturn(abastecimento);
        Abastecimento result = abastecimentoService.salvar(abastecimento);
        assertEquals(abastecimento, result);

        assertThrows(FuelingValidationException.class, () -> abastecimentoService.salvar(null));
    }
}
