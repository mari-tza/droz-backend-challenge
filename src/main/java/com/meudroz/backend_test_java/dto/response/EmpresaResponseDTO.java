package com.meudroz.backend_test_java.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "EmpresaResponseDTO", description = "Dados retornados de uma empresa")
public record EmpresaResponseDTO(

        @Schema(description = "Nome completo da empresa", example = "JAVA TESTE Ltda")
        String nome,

        @Schema(description = "CNPJ da empresa", example = "12345678000112")
        String cnpj,

        @Schema(description = "Endereço físico da empresa", example = "Rua do teste, 123")
        String endereco,

        @Schema(description = "Telefone de contato da empresa", example = "(34) 99999-9999")
        String telefone

) {
}
