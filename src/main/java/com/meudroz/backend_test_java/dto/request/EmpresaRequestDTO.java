package com.meudroz.backend_test_java.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(name = "EmpresaDTO", description = "Objeto que representa os dados de uma empresa")
public record EmpresaRequestDTO(

        @NotBlank(message = "O nome é obrigatório.")
        @Size(max = 100, message = "O nome pode ter no máximo 100 caracteres.")
        @Schema(description = "Nome completo da empresa", example = "JAVA TESTE Ltda", maxLength = 100)
        String nome,

        @NotBlank(message = "O CNPJ é obrigatório.")
        @Pattern(regexp = "\\d{14}", message = "O CNPJ deve ter exatamente 14 dígitos numéricos.")
        @Schema(description = "CNPJ da empresa (apenas números, 14 dígitos)", example = "12345678000112", minLength = 14, maxLength = 14)
        String cnpj,

        @Size(max = 200, message = "O endereço pode ter no máximo 200 caracteres.")
        @Schema(description = "Endereço físico da empresa", example = "Rua do teste, 123", maxLength = 200)
        String endereco,

        @Size(max = 20, message = "O telefone pode ter no máximo 20 caracteres.")
        @Schema(description = "Telefone de contato da empresa", example = "(34) 99999-9999", maxLength = 20)
        String telefone

) {}