package com.telar.TelarMVP.exceptions;

public class CuentaNoEncontradaException extends RuntimeException {

    public CuentaNoEncontradaException(Integer id) {
        super("No existe una cuenta con id " + id);
    }
}
