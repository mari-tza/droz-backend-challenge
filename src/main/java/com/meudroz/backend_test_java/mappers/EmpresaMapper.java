package com.meudroz.backend_test_java.mappers;

import com.meudroz.backend_test_java.domain.Empresa;
import com.meudroz.backend_test_java.dto.request.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.response.EmpresaResponseDTO;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class EmpresaMapper {

    public Empresa toEntity(EmpresaRequestDTO dto) {
        return new Empresa(dto.cnpj(), dto.nome(), dto.endereco(), dto.telefone());
    }

    public EmpresaResponseDTO toResponseDTO(Empresa empresa) {
        return new EmpresaResponseDTO(empresa.getNome(), empresa.getCnpj(), empresa.getEndereco(), empresa.getTelefone());
    }

}
