package com.meudroz.backend_test_java.repository;

import com.meudroz.backend_test_java.domain.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmpresaRepository extends JpaRepository<Empresa, UUID> {
    Optional<Empresa> findByCnpj(String cnpj);
}
