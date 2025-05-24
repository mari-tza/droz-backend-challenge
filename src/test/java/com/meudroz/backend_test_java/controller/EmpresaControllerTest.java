package com.meudroz.backend_test_java.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.meudroz.backend_test_java.dto.request.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.response.EmpresaResponseDTO;
import com.meudroz.backend_test_java.exception.EmpresaNaoEncontradaException;
import com.meudroz.backend_test_java.service.EmpresaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest
public class EmpresaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmpresaService empresaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveListarEmpresasComSucesso() throws Exception {
        var empresa = new EmpresaResponseDTO("Empresa Teste", "12345678000112", "Rua Teste", "(34) 99999-9999");

        when(empresaService.listarEmpresas()).thenReturn(List.of(empresa));

        mockMvc.perform(get("/empresas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome", is("Empresa Teste")))
                .andExpect(jsonPath("$[0].cnpj", is("12345678000112")))
                .andExpect(jsonPath("$[0].endereco", is("Rua Teste")))
                .andExpect(jsonPath("$[0].telefone", is("(34) 99999-9999")));
    }

    @Test
    void deveBuscarEmpresaPorCnpjComSucesso() throws Exception {
        var empresa = new EmpresaResponseDTO("Empresa Teste", "12345678000112", "Rua Teste", "(34) 99999-9999");

        when(empresaService.buscarPorCnpj("12345678000112")).thenReturn(empresa);

        mockMvc.perform(get("/empresas/12345678000112"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Empresa Teste")))
                .andExpect(jsonPath("$.cnpj", is("12345678000112")));
    }

    @Test
    void deveRetornar404QuandoEmpresaNaoEncontrada() throws Exception {
        when(empresaService.buscarPorCnpj("00000000000000"))
                .thenThrow(new EmpresaNaoEncontradaException("Nenhuma empresa encontrada com o CNPJ fornecido."));

        mockMvc.perform(get("/empresas/00000000000000"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveCadastrarEmpresaComSucesso() throws Exception {
        var dto = new EmpresaRequestDTO("Empresa Nova", "12345678000112", "Rua Nova", "(34) 98888-7777");
        var resposta = new EmpresaResponseDTO("Empresa Nova", "12345678000112", "Rua Nova", "(34) 98888-7777");

        when(empresaService.cadastrarEmpresa(any())).thenReturn(resposta);

        mockMvc.perform(post("/empresas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Empresa Nova")))
                .andExpect(jsonPath("$.cnpj", is("12345678000112")));
    }

    @Test
    void deveAtualizarEmpresaComSucesso() throws Exception {
        var dto = new EmpresaRequestDTO("Empresa Atualizada", "12345678000112", "Rua Atualizada", "(34) 97777-6666");

        doNothing().when(empresaService).atualizarEmpresa(eq("12345678000112"), any());

        mockMvc.perform(put("/empresas/12345678000112")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornar404AoAtualizarEmpresaInexistente() throws Exception {
        var dto = new EmpresaRequestDTO("Empresa Inexistente", "00000000000000", "Rua X", "(34) 90000-0000");

        doThrow(new EmpresaNaoEncontradaException("Nenhuma empresa encontrada com o CNPJ fornecido."))
                .when(empresaService).atualizarEmpresa(eq("00000000000000"), any());

        mockMvc.perform(put("/empresas/00000000000000")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }
}
