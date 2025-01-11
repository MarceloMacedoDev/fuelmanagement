package com.example.abastecimento_api.usecase;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.example.abastecimento_api.domain.entity.Abastecimento;
import com.example.abastecimento_api.domain.exceptions.IllegalArgumentException;
import com.example.abastecimento_api.domain.service.AbastecimentoService;
import com.example.abastecimento_api.infrastructure.mapper.AbastecimentoMapper;
import com.example.abastecimento_api.web.dto.AbastecimentoDTO;

@ExtendWith(MockitoExtension.class)
public class GerenciarAbastecimentoUseCaseTest {

    @Mock
    private AbastecimentoService abastecimentoService;

    @Mock
    private AbastecimentoMapper abastecimentoMapper;

    @InjectMocks
    private GerenciarAbastecimentoUseCase gerenciarAbastecimentoUseCase;

    @Test
    void testAdicionarAbastecimento() {
        AbastecimentoDTO abastecimentoDTO = new AbastecimentoDTO();
        abastecimentoDTO.setPlaca("ABC-1234");
        abastecimentoDTO.setQuilometragem(1000);
        abastecimentoDTO.setDataHora(LocalDateTime.now());
        abastecimentoDTO.setValorTotal(50.0);

        Abastecimento abastecimentoEntity = new Abastecimento();
        abastecimentoEntity.setId(1L);
        abastecimentoEntity.setPlaca("ABC-1234");
        abastecimentoEntity.setQuilometragem(1000);
        abastecimentoEntity.setDataHora(LocalDateTime.now());
        abastecimentoEntity.setValorTotal(50.0);

        when(abastecimentoMapper.toEntity(abastecimentoDTO)).thenReturn(abastecimentoEntity);
        when(abastecimentoService.salvar(abastecimentoEntity)).thenReturn(abastecimentoEntity);
        when(abastecimentoMapper.toDTO(abastecimentoEntity)).thenReturn(abastecimentoDTO);

        AbastecimentoDTO resultado = gerenciarAbastecimentoUseCase.adicionarAbastecimento(abastecimentoDTO);

        assertEquals(abastecimentoDTO, resultado);
        verify(abastecimentoService, times(1)).salvar(abastecimentoEntity);
    }

    @Test
    void testGetUltimaQuilometragem() {
        String placa = "ABC-1234";
        Abastecimento abastecimento = new Abastecimento();
        abastecimento.setQuilometragem(1500);

        when(abastecimentoService.getUltimoAbastecimentoPorPlaca(placa)).thenReturn(abastecimento);

        Integer ultimaQuilometragem = gerenciarAbastecimentoUseCase.getUltimaQuilometragem(placa);

        assertEquals(1500, ultimaQuilometragem);
        verify(abastecimentoService, times(1)).getUltimoAbastecimentoPorPlaca(placa);

        when(abastecimentoService.getUltimoAbastecimentoPorPlaca(placa)).thenReturn(null);
        ultimaQuilometragem = gerenciarAbastecimentoUseCase.getUltimaQuilometragem(placa);
        assertEquals(0, ultimaQuilometragem);
    }

    @Test
    void testListarAbastecimentos() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Abastecimento> abastecimentos = List.of(new Abastecimento());
        Page<Abastecimento> pageAbastecimentos = new PageImpl<>(abastecimentos);
        List<AbastecimentoDTO> abastecimentosDTO = List.of(new AbastecimentoDTO());

        when(abastecimentoService.listarTodos(pageable)).thenReturn(pageAbastecimentos);
        when(abastecimentoMapper.toDTO(any(Abastecimento.class))).thenReturn(new AbastecimentoDTO());

        Page<AbastecimentoDTO> resultado = gerenciarAbastecimentoUseCase.listarAbastecimentos(null, pageable);

        assertNotNull(resultado);
        verify(abastecimentoService, times(1)).listarTodos(pageable);
        verify(abastecimentoMapper, times(abastecimentos.size())).toDTO(any(Abastecimento.class));

        when(abastecimentoService.listarPorPlaca(pageable, "ABC-1234")).thenReturn(pageAbastecimentos);
        resultado = gerenciarAbastecimentoUseCase.listarAbastecimentos("ABC-1234", pageable);
        assertNotNull(resultado);
        verify(abastecimentoService, times(1)).listarPorPlaca(pageable, "ABC-1234");

    }

    @Test
    void testRemoverAbastecimento() {
        Long id = 1L;
        gerenciarAbastecimentoUseCase.removerAbastecimento(id);
        verify(abastecimentoService, times(1)).remover(id);
    }

    @Test
    void testAdicionarAbastecimento_quilometragemInvalida() {
        AbastecimentoDTO abastecimentoDTO = new AbastecimentoDTO();
        abastecimentoDTO.setQuilometragem(0);
        assertThrows(IllegalArgumentException.class,
                () -> gerenciarAbastecimentoUseCase.adicionarAbastecimento(abastecimentoDTO));
    }

    @Test
    void testAdicionarAbastecimento_placaInvalida() {
        AbastecimentoDTO abastecimentoDTO = new AbastecimentoDTO();
        abastecimentoDTO.setPlaca("abc1234");
        assertThrows(
                NullPointerException.class,
                () -> gerenciarAbastecimentoUseCase.adicionarAbastecimento(abastecimentoDTO));
    }

    @Test
    void testAdicionarAbastecimento_dataHoraInvalida() {
        AbastecimentoDTO abastecimentoDTO = new AbastecimentoDTO();
        abastecimentoDTO.setDataHora(LocalDateTime.now().plusDays(1));
        assertThrows(
                NullPointerException.class,
                () -> gerenciarAbastecimentoUseCase.adicionarAbastecimento(abastecimentoDTO));
    }

    @Test
    void testAdicionarAbastecimento_valorTotalInvalido() {
        AbastecimentoDTO abastecimentoDTO = new AbastecimentoDTO();
        abastecimentoDTO.setValorTotal(0);
        assertThrows(
                NullPointerException.class,
                () -> gerenciarAbastecimentoUseCase.adicionarAbastecimento(abastecimentoDTO));
    }
}