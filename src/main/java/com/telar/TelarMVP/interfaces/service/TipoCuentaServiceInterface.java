package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.dto.CrearTipoCuentaRequest;
import com.telar.TelarMVP.entities.TipoCuenta;

import java.util.List;

public interface TipoCuentaServiceInterface {
    List<TipoCuenta> listar();
    TipoCuenta findByNombre(String nombre);
    TipoCuenta findById(Integer id);
    TipoCuenta insertar(CrearTipoCuentaRequest request);
}
