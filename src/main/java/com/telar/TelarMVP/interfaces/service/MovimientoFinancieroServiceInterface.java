package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.entities.MovimientoFinanciero;

import java.math.BigDecimal;
import java.util.List;

public interface MovimientoFinancieroServiceInterface {

    MovimientoFinanciero crear(MovimientoFinanciero movimiento);

    List<MovimientoFinanciero> listarPorCuentaId(Integer cuentaId);

    BigDecimal totalActualPorUsuarioYTipoMovimiento(Integer usuarioId, Integer tipoMovimientoId);
}
