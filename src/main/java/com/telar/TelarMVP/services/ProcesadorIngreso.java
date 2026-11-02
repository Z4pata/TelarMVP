package com.telar.TelarMVP.services;

import com.telar.TelarMVP.entities.MovimientoFinanciero;
import com.telar.TelarMVP.interfaces.service.ProcesadorMovimientoInterface;
import org.springframework.stereotype.Component;

@Component
public class ProcesadorIngreso implements ProcesadorMovimientoInterface {

    public static final String CODIGO_TIPO = "Ingreso";

    @Override
    public String getCodigoTipo() {
        return CODIGO_TIPO;
    }

    @Override
    public void procesar(MovimientoFinanciero movimiento) {
    }
}
