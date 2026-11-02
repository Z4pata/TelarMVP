package com.telar.TelarMVP.exceptions;

public class TipoCuentaNoEncontradaException extends RuntimeException {

    public TipoCuentaNoEncontradaException(Integer id) {
        super("No existe un tipo de cuenta con id " + id);
    }
}
