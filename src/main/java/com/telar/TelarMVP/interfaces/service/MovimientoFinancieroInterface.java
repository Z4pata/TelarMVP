package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.entities.MovimientoFinanciero;

import java.util.List;

public interface MovimientoFinancieroInterface {

    MovimientoFinanciero crear(MovimientoFinanciero movimiento);

    List<MovimientoFinanciero> listarPorCuentaId(Integer cuentaId);
}
