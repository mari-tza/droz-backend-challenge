package com.meudroz.backend_test_java.controller;

import java.util.List;
import java.util.Map;

import com.meudroz.backend_test_java.dto.EmpresaDTO;
import com.meudroz.backend_test_java.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/empresas")
@Tag(name = "Empresas", description = "Endpoints para cadastro e consulta de empresas")
public class EmpresaController {

  private final EmpresaService empresaService;

  public EmpresaController(EmpresaService empresaService) {
    this.empresaService = empresaService;
  }

  @Operation(summary = "Listar todas as empresas")
  @ApiResponse(responseCode = "200", description = "Lista de empresas cadastradas",
          content = @Content(mediaType = "application/json",
                  array = @ArraySchema(schema = @Schema(example = """
                {
                  "nome": "JAVA TESTE Ltda",
                  "cnpj": "12.345.678/0001-12",
                  "endereco": "Rua do teste, 123",
                  "telefone": "(34) 99999-9999"
                }
            """))))
  @GetMapping(produces = "application/json")
  public ResponseEntity<List<Map<String, Object>>> listarEmpresas() {
    return ResponseEntity.ok(empresaService.listarEmpresas());
  }

  @Operation(summary = "Buscar uma empresa pelo CNPJ")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Empresa encontrada",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "nome": "JAVA TESTE Ltda",
                  "cnpj": "12.345.678/0001-12",
                  "endereco": "Rua do teste, 123",
                  "telefone": "(34) 99999-9999"
                }
            """))),
          @ApiResponse(responseCode = "404", description = "Empresa não encontrada",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "erro": "Empresa não encontrada com o CNPJ fornecido."
                }
            """))),
          @ApiResponse(responseCode = "500", description = "Erro interno inesperado",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "erro": "Erro interno inesperado."
                }
            """)))
  })
  @GetMapping(value = "/{cnpj}", produces = "application/json")
  public ResponseEntity<Object> buscarPorCnpj(@PathVariable String cnpj) {
    return ResponseEntity.ok(empresaService.buscarPorCnpj(cnpj));
  }

  @Operation(summary = "Cadastrar uma nova empresa")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Empresa cadastrada com sucesso",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "mensagem": "Empresa cadastrada com sucesso.",
                  "linhasAfetadas": 1
                }
            """))),
          @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "nome": "O nome é obrigatório.",
                  "cnpj": "O CNPJ deve ter exatamente 14 dígitos numéricos."
                }
            """))),
          @ApiResponse(responseCode = "500", description = "Erro interno inesperado",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "erro": "Erro interno inesperado."
                }
            """)))
  })
  @PostMapping(consumes = "application/json", produces = "application/json")
  public ResponseEntity<Map<String, Object>> cadastrarEmpresa(@RequestBody @Valid EmpresaDTO empresa) {
    return ResponseEntity.ok(empresaService.cadastrarEmpresa(empresa));
  }

  @Operation(summary = "Atualizar dados de uma empresa pelo CNPJ")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Empresa atualizada com sucesso",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "mensagem": "Empresa atualizada com sucesso.",
                  "linhasAfetadas": 1
                }
            """))),
          @ApiResponse(responseCode = "400", description = "Erro de validação nos dados enviados",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "nome": "O nome é obrigatório.",
                  "endereco": "O endereço pode ter no máximo 200 caracteres."
                }
            """))),
          @ApiResponse(responseCode = "404", description = "Empresa não encontrada",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "erro": "Nenhuma empresa encontrada com o CNPJ fornecido."
                }
            """))),
          @ApiResponse(responseCode = "500", description = "Erro interno inesperado",
                  content = @Content(mediaType = "application/json", schema = @Schema(example = """
                {
                  "erro": "Erro interno inesperado."
                }
            """)))
  })
  @PutMapping(value = "/{cnpj}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<Map<String, Object>> atualizarEmpresa(@PathVariable String cnpj, @RequestBody @Valid EmpresaDTO empresa) {
    return ResponseEntity.ok(empresaService.atualizarEmpresa(cnpj, empresa));
  }
}