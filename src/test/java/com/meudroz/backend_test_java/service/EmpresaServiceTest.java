package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.domain.Empresa;
import com.meudroz.backend_test_java.dto.request.EmpresaRequestDTO;
import com.meudroz.backend_test_java.exception.EmpresaNaoEncontradaException;
import com.meudroz.backend_test_java.mappers.EmpresaMapper;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class EmpresaServiceTest {

    private EmpresaRepository empresaRepository;
    private EmpresaMapper empresaMapper;
    private EmpresaService empresaService;

    @BeforeEach
    void setUp() {
        empresaRepository = mock(EmpresaRepository.class);
        empresaMapper = new EmpresaMapper();
        empresaService = new EmpresaService(empresaRepository, empresaMapper);
    }

    @Test
    void deveListarEmpresasComSucesso() {
        var empresa = new Empresa(
                UUID.randomUUID(),
                "12345678000112",
                "Empresa Teste",
                "Rua Teste",
                "(34) 99999-9999"
        );

        when(empresaRepository.findAll()).thenReturn(List.of(empresa));

        var resultado = empresaService.listarEmpresas();

        assertEquals(1, resultado.size());
        var dto = resultado.getFirst();

        assertAll(
                () -> assertEquals("Empresa Teste", dto.nome()),
                () -> assertEquals("12345678000112", dto.cnpj()),
                () -> assertEquals("Rua Teste", dto.endereco()),
                () -> assertEquals("(34) 99999-9999", dto.telefone())
        );
    }

    @Test
    void deveRetornarEmpresaPeloCnpj() {
        var empresa = new Empresa(
                UUID.randomUUID(),
                "12345678000112",
                "Empresa Teste",
                "Rua Teste",
                "(34) 99999-9999"
        );

        when(empresaRepository.findByCnpj("12345678000112")).thenReturn(Optional.of(empresa));

        var resultado = empresaService.buscarPorCnpj("12345678000112");

        assertAll(
                () -> assertEquals("Empresa Teste", resultado.nome()),
                () -> assertEquals("12345678000112", resultado.cnpj())
        );
    }

    @Test
    void deveLancarExcecaoQuandoEmpresaNaoEncontrada() {
        when(empresaRepository.findByCnpj("00000000000000")).thenReturn(Optional.empty());

        var excecao = assertThrows(
                EmpresaNaoEncontradaException.class,
                () -> empresaService.buscarPorCnpj("00000000000000")
        );

        assertEquals("Nenhuma empresa encontrada com o CNPJ fornecido.", excecao.getMessage());
    }

    @Test
    void deveCadastrarEmpresaComSucesso() {
        var dto = new EmpresaRequestDTO("Empresa Nova", "12345678000112", "Rua Nova", "(34) 98888-7777");
        var entity = new Empresa(UUID.randomUUID(), dto.cnpj(), dto.nome(), dto.endereco(), dto.telefone());

        when(empresaRepository.save(any(Empresa.class))).thenReturn(entity);

        var resultado = empresaService.cadastrarEmpresa(dto);

        assertAll(
                () -> assertEquals("Empresa Nova", resultado.nome()),
                () -> assertEquals("12345678000112", resultado.cnpj())
        );
    }

    @Test
    void deveAtualizarEmpresaComSucesso() {
        var dto = new EmpresaRequestDTO("Empresa Atualizada", "12345678000112", "Rua Nova", "(34) 98888-7777");
        var entity = new Empresa(UUID.randomUUID(), dto.cnpj(), "Antigo Nome", "End Antigo", "9999");

        when(empresaRepository.findByCnpj(dto.cnpj())).thenReturn(Optional.of(entity));
        when(empresaRepository.save(any(Empresa.class))).thenReturn(entity);

        assertDoesNotThrow(() -> empresaService.atualizarEmpresa(dto.cnpj(), dto));

        verify(empresaRepository).save(argThat(e ->
                e.getNome().equals(dto.nome()) &&
                        e.getEndereco().equals(dto.endereco()) &&
                        e.getTelefone().equals(dto.telefone())
        ));
    }

    @Test
    void deveLancarExcecaoAoAtualizarEmpresaInexistente() {
        var dto = new EmpresaRequestDTO("Empresa Inexistente", "00000000000000", "Rua X", "3499999999");

        when(empresaRepository.findByCnpj(dto.cnpj())).thenReturn(Optional.empty());

        var ex = assertThrows(
                EmpresaNaoEncontradaException.class,
                () -> empresaService.atualizarEmpresa(dto.cnpj(), dto)
        );

        assertEquals("Nenhuma empresa encontrada com o CNPJ fornecido.", ex.getMessage());
        verify(empresaRepository, never()).save(any());
    }
}