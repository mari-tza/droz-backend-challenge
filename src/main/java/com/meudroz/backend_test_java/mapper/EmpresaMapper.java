package com.meudroz.backend_test_java.mapper;

import com.meudroz.backend_test_java.dto.request.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.response.EmpresaResponseDTO;
import com.meudroz.backend_test_java.entity.Empresa;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaRequestDTO dto) {
        return Empresa.builder()
                .cnpj(dto.cnpj())
                .nome(dto.nome())
                .endereco(dto.endereco())
                .telefone(dto.telefone())
                .build();
    }

    public EmpresaResponseDTO toResponseDTO(Empresa empresa) {
        return new EmpresaResponseDTO(empresa.getNome(), empresa.getCnpj(), empresa.getEndereco(), empresa.getTelefone());
    }

}
