package com.telar.TelarMVP.exceptions;

public class TipoMovimientoNoSoportadoException extends RuntimeException {

    public TipoMovimientoNoSoportadoException(String tipo) {
        super("No existe una estrategia para el tipo de movimiento " + tipo);
    }
}
