package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.dto.TipoMovimientoRequest;
import com.telar.TelarMVP.entities.TipoMovimiento;

import java.util.List;

public interface TipoMovimientoServiceInterface {

    List<TipoMovimiento> listar();

    TipoMovimiento findByNombre(String nombre);

    TipoMovimiento findById(Integer id);

    TipoMovimiento insertar(TipoMovimientoRequest request);

    TipoMovimiento actualizar(Integer id, TipoMovimientoRequest request);

    void eliminar(Integer id);
}
