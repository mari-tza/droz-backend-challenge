package com.meudroz.backend_test_java.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "empresas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(length = 14, nullable = false, unique = true)
    private String cnpj;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 200)
    private String endereco;

    @Column(length = 20)
    private String telefone;
}