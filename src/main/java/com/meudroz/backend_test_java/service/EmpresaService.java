package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.domain.Empresa;
import com.meudroz.backend_test_java.dto.request.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.response.EmpresaResponseDTO;
import com.meudroz.backend_test_java.exception.EmpresaNaoEncontradaException;
import com.meudroz.backend_test_java.mappers.EmpresaMapper;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmpresaService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);
    private final EmpresaRepository repository;
    private final EmpresaMapper empresaMapper;

    public List<EmpresaResponseDTO> listarEmpresas() {
        log.info("Listando todas as empresas cadastradas");
        return repository.findAll().stream()
                .map(empresaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public EmpresaResponseDTO buscarPorCnpj(String cnpj) {
        log.info("Buscando empresa pelo CNPJ: {}", cnpj);
        Empresa empresa = repository.findByCnpj(cnpj)
                .orElseThrow(() -> {
                    log.warn("Empresa com CNPJ {} não encontrada", cnpj);
                    return new EmpresaNaoEncontradaException("Nenhuma empresa encontrada com o CNPJ fornecido.");
                });

        return empresaMapper.toResponseDTO(empresa);
    }

    public EmpresaResponseDTO cadastrarEmpresa(EmpresaRequestDTO dto) {
        log.info("Cadastrando empresa: {}", dto.nome());

        Empresa novaEmpresa = empresaMapper.toEntity(dto);
        Empresa salva = repository.save(novaEmpresa);

        log.info("Empresa cadastrada com sucesso: {}", salva.getCnpj());
        return empresaMapper.toResponseDTO(salva);
    }

    public void atualizarEmpresa(String cnpj, EmpresaRequestDTO dto) {
        log.info("Atualizando empresa de CNPJ {} para nome {}", cnpj, dto.nome());

        Empresa existente = repository.findByCnpj(cnpj)
                .orElseThrow(() -> {
                    log.warn("Empresa com CNPJ {} não encontrada", cnpj);
                    return new EmpresaNaoEncontradaException("Nenhuma empresa encontrada com o CNPJ fornecido.");
                });

        existente.setNome(dto.nome());
        existente.setEndereco(dto.endereco());
        existente.setTelefone(dto.telefone());

        repository.save(existente);

        log.info("Empresa atualizada com sucesso: {}", existente.getCnpj());
    }
}