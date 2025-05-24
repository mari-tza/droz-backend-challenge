package com.meudroz.backend_test_java.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @Column(length = 14, nullable = false, unique = true)
    private String cnpj;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 200)
    private String endereco;

    @Column(length = 20)
    private String telefone;

    public Empresa() {
    }

    public Empresa(String cnpj, String nome, String endereco, String telefone) {
        this.cnpj = cnpj;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
    }
}