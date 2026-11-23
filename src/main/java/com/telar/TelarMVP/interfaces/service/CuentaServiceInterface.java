package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.entities.Cuenta;

import java.util.List;

public interface CuentaServiceInterface {

    List<Cuenta> listar();

    List<Cuenta> listarPorUsuarioId(Integer usuarioId);

    /** Busca una cuenta del usuario; si es de otro usuario se trata como inexistente. */
    Cuenta findById(Integer id, Integer usuarioId);

    Cuenta insertar(Cuenta cuenta);

    Cuenta actualizar(Integer id, Cuenta cuenta);

    void eliminar(Integer id, Integer usuarioId);
}
