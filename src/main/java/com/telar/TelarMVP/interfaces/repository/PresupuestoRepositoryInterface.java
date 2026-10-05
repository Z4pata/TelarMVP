package com.telar.TelarMVP.interfaces.repository;

import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.Presupuesto;

public interface PresupuestoRepositoryInterface {
    Presupuesto crear(Presupuesto presupuesto);
    PresupuestoResponse buscarActualPorUsuario(Integer userId);
    Boolean existeActivo(Integer userId);
    // TODO
    //Presupuesto actualizar(Presupuesto presupuesto);
    //Presupuesto eliminar(Integer id);
    //List<Presupuesto> buscarTodosPorUsuario(Integer userId);
}
