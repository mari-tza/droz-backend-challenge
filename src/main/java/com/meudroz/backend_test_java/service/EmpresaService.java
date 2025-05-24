package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.dto.request.EmpresaRequestDTO;
import com.meudroz.backend_test_java.exception.EmpresaNaoEncontradaException;
import com.meudroz.backend_test_java.repository.EmpresaRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpresaService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);
    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) {
        this.repository = repository;
    }

    public List<Map<String, Object>> listarEmpresas() {
        log.info("Listando todas as empresas cadastradas");
        List<Map<String, Object>> empresas = repository.findAll();
        empresas.forEach(this::formatarCnpj);
        return empresas;
    }

    public Map<String, Object> buscarPorCnpj(String cnpj) {
        log.info("Buscando empresa pelo CNPJ: {}", cnpj);
//        List<Map<String, Object>> resultado = repository.findByCnpj(cnpj);
//        if (resultado.isEmpty()) {
//            log.warn("Empresa com CNPJ {} não encontrada", cnpj);
//            return Map.of("erro", "Empresa não encontrada com o CNPJ fornecido.");
//        }
//        Map<String, Object> empresa = resultado.get(0);
//        formatarCnpj(empresa);
        return null;
    }

    public Map<String, Object> cadastrarEmpresa(EmpresaRequestDTO empresa) {
        log.info("Cadastrando empresa: {}", empresa.nome());
        Map<String, Object> response = new HashMap<>();
        String cnpjLimpo = empresa.cnpj().replaceAll("[^0-9]", "");
        int rows = repository.save(empresa, cnpjLimpo);
        response.put("mensagem", "Empresa cadastrada com sucesso.");
        response.put("linhasAfetadas", rows);
        return response;
    }

    public void atualizarEmpresa(String cnpj, EmpresaRequestDTO empresaRequestDTO) {
        log.info("Atualizando empresa de CNPJ {} para nome {}", cnpj, empresaRequestDTO.nome());

        repository.findByCnpj(cnpj)
                .orElseThrow(() -> new EmpresaNaoEncontradaException("Nenhuma empresa encontrada com o CNPJ fornecido."));

        repository.update(empresaRequestDTO, cnpj);
        log.info("Empresa atualizada com sucesso.");
    }

    private void formatarCnpj(Map<String, Object> empresa) {
        String cnpj = (String) empresa.get("cnpj");
        empresa.put("cnpj", cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5"));
    }
}