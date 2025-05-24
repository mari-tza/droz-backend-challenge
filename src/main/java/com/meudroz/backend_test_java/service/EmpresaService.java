package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.entity.Empresa;
import com.meudroz.backend_test_java.dto.request.EmpresaRequestDTO;
import com.meudroz.backend_test_java.dto.response.EmpresaResponseDTO;
import com.meudroz.backend_test_java.exception.CnpjJaCadastradoException;
import com.meudroz.backend_test_java.exception.EmpresaNaoEncontradaException;
import com.meudroz.backend_test_java.mapper.EmpresaMapper;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmpresaService {

    private static final String EMPRESA_NAO_ENCONTRADA = "Empresa com CNPJ {} não encontrada";

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
                    log.warn(EMPRESA_NAO_ENCONTRADA, cnpj);
                    return new EmpresaNaoEncontradaException("Nenhuma empresa encontrada com o CNPJ fornecido.");
                });

        return empresaMapper.toResponseDTO(empresa);
    }

    public EmpresaResponseDTO cadastrarEmpresa(EmpresaRequestDTO dto) {
        log.info("Cadastrando empresa: {}", dto.nome());

        repository.findByCnpj(dto.cnpj()).ifPresent(empresa -> {
            throw new CnpjJaCadastradoException("Já existe uma empresa cadastrada com esse CNPJ.");
        });

        Empresa novaEmpresa = empresaMapper.toEntity(dto);
        Empresa salva = repository.save(novaEmpresa);

        log.info("Empresa cadastrada com sucesso: {}", salva.getCnpj());
        return empresaMapper.toResponseDTO(salva);
    }

    public void atualizarEmpresa(String cnpj, EmpresaRequestDTO dto) {
        log.info("Atualizando empresa de CNPJ {} para nome {}", cnpj, dto.nome());

        Empresa existente = repository.findByCnpj(cnpj)
                .orElseThrow(() -> {
                    log.warn(EMPRESA_NAO_ENCONTRADA, cnpj);
                    return new EmpresaNaoEncontradaException("Nenhuma empresa encontrada com o CNPJ fornecido.");
                });

        existente.setNome(dto.nome());
        existente.setEndereco(dto.endereco());
        existente.setTelefone(dto.telefone());

        repository.save(existente);

        log.info("Empresa atualizada com sucesso: {}", existente.getCnpj());
    }
}