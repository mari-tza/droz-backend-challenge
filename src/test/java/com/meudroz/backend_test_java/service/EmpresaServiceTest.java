package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.dto.EmpresaDTO;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EmpresaServiceTest {

    private EmpresaRepository empresaRepository;
    private EmpresaService empresaService;

    @BeforeEach
    void setUp() {
        empresaRepository = mock(EmpresaRepository.class);
        empresaService = new EmpresaService(empresaRepository);
    }

    @Test
    void deveListarEmpresasComCnpjFormatado() {
        Map<String, Object> empresa = new HashMap<>();
        empresa.put("nome", "Empresa Teste");
        empresa.put("cnpj", "12345678000112");
        empresa.put("endereco", "Rua Teste");
        empresa.put("telefone", "34999999999");

        when(empresaRepository.findAll()).thenReturn(List.of(empresa));

        List<Map<String, Object>> resultado = empresaService.listarEmpresas();

        assertEquals(1, resultado.size());
        assertEquals("Empresa Teste", resultado.get(0).get("nome"));
        assertEquals("12.345.678/0001-12", resultado.get(0).get("cnpj"));
    }

    @Test
    void deveRetornarEmpresaPeloCnpj() {
        String cnpj = "12345678000112";
        Map<String, Object> empresa = new HashMap<>();
        empresa.put("nome", "Empresa Teste");
        empresa.put("cnpj", "12345678000112");
        empresa.put("endereco", "Rua Teste");
        empresa.put("telefone", "34999999999");

        when(empresaRepository.findByCnpj(cnpj)).thenReturn(List.of(empresa));

        Map<String, Object> resultado = empresaService.buscarPorCnpj(cnpj);

        assertEquals("Empresa Teste", resultado.get("nome"));
        assertEquals("12.345.678/0001-12", resultado.get("cnpj"));
    }

    @Test
    void deveRetornarErroSeEmpresaNaoForEncontrada() {
        String cnpj = "00000000000000";
        when(empresaRepository.findByCnpj(cnpj)).thenReturn(List.of());

        Map<String, Object> resultado = empresaService.buscarPorCnpj(cnpj);

        assertEquals("Empresa não encontrada com o CNPJ fornecido.", resultado.get("erro"));
    }

    @Test
    void deveCadastrarEmpresaComSucesso() {
        EmpresaDTO dto = new EmpresaDTO("Empresa Nova", "12345678000112", "Rua Nova", "34988887777");
        when(empresaRepository.save(eq(dto), anyString())).thenReturn(1);

        Map<String, Object> resultado = empresaService.cadastrarEmpresa(dto);

        assertEquals("Empresa cadastrada com sucesso.", resultado.get("mensagem"));
        assertEquals(1, resultado.get("linhasAfetadas"));
    }

    @Test
    void deveAtualizarEmpresaComSucesso() {
        EmpresaDTO dto = new EmpresaDTO("Empresa Atualizada", "12345678000112", "Rua Nova", "34988887777");
        when(empresaRepository.update(eq(dto), eq(dto.cnpj()))).thenReturn(1);

        Map<String, Object> resultado = empresaService.atualizarEmpresa(dto.cnpj(), dto);

        assertEquals("Empresa atualizada com sucesso.", resultado.get("mensagem"));
        assertEquals(1, resultado.get("linhasAfetadas"));
    }

    @Test
    void deveRetornarErroAoAtualizarEmpresaInexistente() {
        EmpresaDTO dto = new EmpresaDTO("Empresa Qualquer", "00000000000000", "Rua X", "3488888888");
        when(empresaRepository.update(eq(dto), eq(dto.cnpj()))).thenReturn(0);

        Map<String, Object> resultado = empresaService.atualizarEmpresa(dto.cnpj(), dto);

        assertEquals("Nenhuma empresa encontrada com o CNPJ fornecido.", resultado.get("erro"));
    }
}