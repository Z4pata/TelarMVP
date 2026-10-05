package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.dto.CrearPresupuestoRequest;
import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.Presupuesto;

public interface PresupuestoServiceInterface {
    Presupuesto crear(CrearPresupuestoRequest request);
    PresupuestoResponse buscarActualPorUsuario(Integer usuarioId);
}
