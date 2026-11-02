package com.telar.TelarMVP.interfaces.service;

import com.telar.TelarMVP.entities.MovimientoFinanciero;

public interface ProcesadorMovimientoInterface {

    String getCodigoTipo();

    void procesar(MovimientoFinanciero movimiento);
}
