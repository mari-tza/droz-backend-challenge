package com.meudroz.backend_test_java.repository;

import com.meudroz.backend_test_java.dto.request.EmpresaRequestDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    public Optional<EmpresaRequestDTO> findByCnpj(String cnpj) {
        String sql = "SELECT nome, cnpj, endereco, telefone FROM empresas WHERE cnpj = ?";
        List<EmpresaRequestDTO> resultados = jdbcTemplate.query(sql, new Object[]{cnpj},
                (rs, rowNum) -> new EmpresaRequestDTO(
                        rs.getString("nome"),
                        rs.getString("cnpj"),
                        rs.getString("endereco"),
                        rs.getString("telefone")
                ));

        return resultados.stream().findFirst();
    }

    public int save(EmpresaRequestDTO empresa, String cnpjLimpo) {
        String sql = "INSERT INTO empresas (nome, cnpj, endereco, telefone) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql,
                empresa.nome(), cnpjLimpo, empresa.endereco(), empresa.telefone());
    }

    public void update(EmpresaRequestDTO empresa, String cnpj) {
        String sql = "UPDATE empresas SET nome = ?, endereco = ?, telefone = ? WHERE cnpj = ?";
        jdbcTemplate.update(sql,
                empresa.nome(), empresa.endereco(), empresa.telefone(), cnpj);
    }
}