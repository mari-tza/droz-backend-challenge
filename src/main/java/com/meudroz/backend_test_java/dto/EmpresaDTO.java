package com.meudroz.backend_test_java.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "EmpresaDTO", description = "Dados da empresa")
public record EmpresaDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres.")
        @Schema(description = "Nome da empresa", example = "JAVA TESTE Ltda")
        String nome,

        @NotBlank(message = "O CNPJ é obrigatório.")
        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve ter exatamente 14 dígitos numéricos.")
        @Schema(description = "CNPJ da empresa", example = "12345678000112")
        String cnpj,

        @Size(max = 200, message = "O endereço pode ter no máximo 200 caracteres.")
        @Schema(description = "Endereço da empresa", example = "Rua do teste, 123")
        String endereco,

        @Size(max = 20, message = "O telefone pode ter no máximo 20 caracteres.")
        @Schema(description = "Telefone da empresa", example = "(34) 99999-9999")
        String telefone

) {}