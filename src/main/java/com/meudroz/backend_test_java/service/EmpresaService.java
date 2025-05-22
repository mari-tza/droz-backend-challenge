package com.meudroz.backend_test_java.service;

import com.meudroz.backend_test_java.dto.EmpresaDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmpresaService {

    private static final Logger log = LoggerFactory.getLogger(EmpresaService.class);
    private final JdbcTemplate jdbcTemplate;

    public EmpresaService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> listarEmpresas() {
        log.info("Listando todas as empresas cadastradas");
        String sql = "SELECT nome, cnpj, endereco, telefone FROM empresas";
        List<Map<String, Object>> empresas = jdbcTemplate.queryForList(sql);

        for (Map<String, Object> empresa : empresas) {
            formatarCnpj(empresa);
        }

        return empresas;
    }

    public Map<String, Object> buscarPorCnpj(String cnpj) {
        log.info("Buscando empresa pelo CNPJ: {}", cnpj);
        String sql = "SELECT nome, cnpj, endereco, telefone FROM empresas WHERE cnpj = ?";
        List<Map<String, Object>> resultado = jdbcTemplate.queryForList(sql, cnpj);

        if (resultado.isEmpty()) {
            log.warn("Empresa com CNPJ {} não encontrada", cnpj);
            return Map.of("erro", "Empresa não encontrada com o CNPJ fornecido.");
        }

        Map<String, Object> empresa = resultado.get(0);
        formatarCnpj(empresa);
        return empresa;
    }

    public Map<String, Object> cadastrarEmpresa(EmpresaDTO empresa) {
        log.info("Cadastrando empresa: {}", empresa.nome());
        Map<String, Object> response = new HashMap<>();

        String cnpjLimpo = empresa.cnpj().replaceAll("[^0-9]", "");

        String sql = "INSERT INTO empresas (nome, cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";
        int rows = jdbcTemplate.update(sql,
                empresa.nome(),
                cnpjLimpo,
                empresa.endereco(),
                empresa.telefone());

        log.info("Empresa {} cadastrada com sucesso ({} linha(s) afetada(s))", empresa.nome(), rows);
        response.put("mensagem", "Empresa cadastrada com sucesso.");
        response.put("linhasAfetadas", rows);
        return response;
    }

    public Map<String, Object> atualizarEmpresa(String cnpj, EmpresaDTO empresa) {
        log.info("Atualizando empresa de CNPJ {} para nome {}", cnpj, empresa.nome());
        Map<String, Object> response = new HashMap<>();

        String sql = "UPDATE empresas SET nome = ?, endereco = ?, telefone = ? WHERE cnpj = ?";
        int rows = jdbcTemplate.update(sql,
                empresa.nome(),
                empresa.endereco(),
                empresa.telefone(),
                cnpj);

        if (rows == 0) {
            log.warn("Nenhuma empresa encontrada para o CNPJ {}", cnpj);
            response.put("erro", "Nenhuma empresa encontrada com o CNPJ fornecido.");
            return response;
        }

        log.info("Empresa com CNPJ {} atualizada com sucesso ({} linha(s) afetada(s))", cnpj, rows);
        response.put("mensagem", "Empresa atualizada com sucesso.");
        response.put("linhasAfetadas", rows);
        return response;
    }

    private void formatarCnpj(Map<String, Object> empresa) {
        String cnpj = (String) empresa.get("cnpj");
        empresa.put("cnpj", cnpj.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})", "$1.$2.$3/$4-$5"));
    }
}