package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.entities.Cuenta;

import java.util.List;

public interface CuentaServiceInterface {

    List<Cuenta> listar();

    List<Cuenta> listarPorUsuarioId(Integer usuarioId);

    Cuenta findById(Integer id);

    Cuenta insertar(Cuenta cuenta);

    Cuenta actualizar(Integer id, Cuenta cuenta);

    void eliminar(Integer id);
}
