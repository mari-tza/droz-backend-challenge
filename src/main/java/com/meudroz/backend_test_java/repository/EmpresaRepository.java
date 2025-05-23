package com.meudroz.backend_test_java.repository;

import com.meudroz.backend_test_java.dto.EmpresaDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class EmpresaRepository {

    private final JdbcTemplate jdbcTemplate;

    public EmpresaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findAll() {
        String sql = "SELECT nome, cnpj, endereco, telefone FROM empresas";
        return jdbcTemplate.queryForList(sql);
    }

    public List<Map<String, Object>> findByCnpj(String cnpj) {
        String sql = "SELECT nome, cnpj, endereco, telefone FROM empresas WHERE cnpj = ?";
        return jdbcTemplate.queryForList(sql, cnpj);
    }

    public int save(EmpresaDTO empresa, String cnpjLimpo) {
        String sql = "INSERT INTO empresas (nome, cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                empresa.nome(), cnpjLimpo, empresa.endereco(), empresa.telefone());
    }

    public int update(EmpresaDTO empresa, String cnpj) {
        String sql = "UPDATE empresas SET nome = ?, endereco = ?, telefone = ? WHERE cnpj = ?";
        return jdbcTemplate.update(sql,
                empresa.nome(), empresa.endereco(), empresa.telefone(), cnpj);
    }
}