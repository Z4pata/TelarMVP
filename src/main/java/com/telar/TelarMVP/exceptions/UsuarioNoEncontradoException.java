package com.telar.TelarMVP.exceptions;

public class UsuarioNoEncontradoException extends RuntimeException {

    public UsuarioNoEncontradoException(Integer id) {
        super("No existe un usuario con id " + id);
    }
}
