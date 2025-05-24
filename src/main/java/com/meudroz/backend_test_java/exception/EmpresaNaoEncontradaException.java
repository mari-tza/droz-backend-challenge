package com.meudroz.backend_test_java.exception;

public class EmpresaNaoEncontradaException extends RuntimeException {
    public EmpresaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
