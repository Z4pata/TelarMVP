package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.entities.MovimientoFinanciero;

import java.math.BigDecimal;
import java.util.List;

public interface MovimientoFinancieroServiceInterface {

    MovimientoFinanciero crear(MovimientoFinanciero movimiento, Integer usuarioId);

    List<MovimientoFinanciero> listarPorCuentaId(Integer cuentaId, Integer usuarioId);

    BigDecimal totalActualPorUsuarioYTipoMovimiento(Integer usuarioId, Integer tipoMovimientoId);
}
