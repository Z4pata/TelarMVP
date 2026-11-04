package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.MovimientoFinanciero;
import com.telar.TelarMVP.repositories.MovimientoFinancieroRepository;
import com.telar.TelarMVP.interfaces.repository.PresupuestoRepositoryInterface;
import com.telar.TelarMVP.interfaces.service.ProcesadorMovimientoInterface;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class ProcesadorEgreso implements ProcesadorMovimientoInterface {

    public static final String CODIGO_TIPO = "Egreso";
    private static final String AVISO_PRESUPUESTO = "Aviso: superaste el presupuesto.";

    private final MovimientoFinancieroRepository movimientoRepository;
    private final PresupuestoRepositoryInterface presupuestoRepository;

    public ProcesadorEgreso(
            MovimientoFinancieroRepository movimientoRepository,
            PresupuestoRepositoryInterface presupuestoRepository
    ) {
        this.movimientoRepository = movimientoRepository;
        this.presupuestoRepository = presupuestoRepository;
    }

    @Override
    public String getCodigoTipo() {
        return CODIGO_TIPO;
    }

    @Override
    public void procesar(MovimientoFinanciero movimiento) {
        Integer usuarioId = movimiento.getCuenta().getUsuarioId();
        if (!presupuestoRepository.existeActivo(usuarioId)) {
            return;
        }

        PresupuestoResponse presupuesto = presupuestoRepository.buscarActualPorUsuario(usuarioId);
        LocalDate primerDiaMes = movimiento.getFecha().toLocalDate().withDayOfMonth(1);
        LocalDateTime inicioMes = primerDiaMes.atStartOfDay();
        LocalDateTime inicioMesSiguiente = primerDiaMes.plusMonths(1).atStartOfDay();
        BigDecimal gastosPrevios = movimientoRepository.sumarPorUsuarioTipoYPeriodo(
                usuarioId,
                getCodigoTipo(),
                inicioMes,
                inicioMesSiguiente
        );

        if (gastosPrevios.add(movimiento.getMonto())
                .compareTo(BigDecimal.valueOf(presupuesto.getMontoLimite())) > 0) {
            agregarAviso(movimiento);
        }
    }

    private void agregarAviso(MovimientoFinanciero movimiento) {
        String descripcion = movimiento.getDescripcion();
        if (descripcion != null && descripcion.contains(AVISO_PRESUPUESTO)) {
            return;
        }

        String separador = descripcion == null || descripcion.isBlank() ? "" : "\n";
        int maximoBase = 255 - separador.length() - AVISO_PRESUPUESTO.length();
        String base = descripcion == null ? "" : descripcion;
        if (base.length() > maximoBase) {
            base = base.substring(0, maximoBase);
        }

        movimiento.setDescripcion(base + separador + AVISO_PRESUPUESTO);
    }
}
