package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.entities.MovimientoFinanciero;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.repositories.MovimientoFinancieroRepository;
import com.telar.TelarMVP.interfaces.repository.PresupuestoRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcesadorEgresoTests {

    @Mock
    private MovimientoFinancieroRepository movimientoRepository;

    @Mock
    private PresupuestoRepositoryInterface presupuestoRepository;

    private ProcesadorEgreso procesador;

    @BeforeEach
    void setUp() {
        procesador = new ProcesadorEgreso(movimientoRepository, presupuestoRepository);
    }

    @Test
    void agregaAvisoCuandoElMovimientoHaceSuperarElPresupuesto() {
        MovimientoFinanciero movimiento = movimiento(new BigDecimal("20.00"), "Compra");
        when(presupuestoRepository.existeActivo(10)).thenReturn(true);
        when(presupuestoRepository.buscarActualPorUsuario(10)).thenReturn(presupuesto());
        when(movimientoRepository.sumarPorUsuarioTipoYPeriodo(
                10,
                ProcesadorEgreso.CODIGO_TIPO,
                LocalDateTime.of(2026, 11, 1, 0, 0),
                LocalDateTime.of(2026, 12, 1, 0, 0)
        )).thenReturn(new BigDecimal("90.01"));

        procesador.procesar(movimiento);

        assertEquals("Compra\nAviso: superaste el presupuesto.", movimiento.getDescripcion());
    }

    @Test
    void noAgregaAvisoSiElGastoNoSuperaElLimite() {
        MovimientoFinanciero movimiento = movimiento(new BigDecimal("10.00"), "Compra");
        when(presupuestoRepository.existeActivo(10)).thenReturn(true);
        when(presupuestoRepository.buscarActualPorUsuario(10)).thenReturn(presupuesto());
        when(movimientoRepository.sumarPorUsuarioTipoYPeriodo(
                10,
                ProcesadorEgreso.CODIGO_TIPO,
                LocalDateTime.of(2026, 11, 1, 0, 0),
                LocalDateTime.of(2026, 12, 1, 0, 0)
        )).thenReturn(new BigDecimal("90.00"));

        procesador.procesar(movimiento);

        assertEquals("Compra", movimiento.getDescripcion());
    }

    @Test
    void noConsultaGastosCuandoNoHayPresupuestoActivo() {
        MovimientoFinanciero movimiento = movimiento(new BigDecimal("10.00"), "Compra");
        when(presupuestoRepository.existeActivo(10)).thenReturn(false);

        procesador.procesar(movimiento);

        assertEquals("Compra", movimiento.getDescripcion());
        verify(movimientoRepository, never()).sumarPorUsuarioTipoYPeriodo(
                org.mockito.ArgumentMatchers.eq(10),
                org.mockito.ArgumentMatchers.anyString(),
                org.mockito.ArgumentMatchers.any(),
                org.mockito.ArgumentMatchers.any()
        );
    }

    @Test
    void mantieneLaDescripcionDentroDelLimiteDeLaBaseDeDatos() {
        MovimientoFinanciero movimiento = movimiento(new BigDecimal("20.00"), "x".repeat(255));
        when(presupuestoRepository.existeActivo(10)).thenReturn(true);
        when(presupuestoRepository.buscarActualPorUsuario(10)).thenReturn(presupuesto());
        when(movimientoRepository.sumarPorUsuarioTipoYPeriodo(
                10,
                ProcesadorEgreso.CODIGO_TIPO,
                LocalDateTime.of(2026, 11, 1, 0, 0),
                LocalDateTime.of(2026, 12, 1, 0, 0)
        )).thenReturn(new BigDecimal("90.01"));

        procesador.procesar(movimiento);

        assertEquals(255, movimiento.getDescripcion().length());
        assertEquals("Aviso: superaste el presupuesto.",
                movimiento.getDescripcion().substring(movimiento.getDescripcion().indexOf('\n') + 1));
    }

    private MovimientoFinanciero movimiento(BigDecimal monto, String descripcion) {
        return new MovimientoFinanciero(
                null,
                null,
                new Cuenta(1, 10, null, "Banco", null),
                new TipoMovimiento(2, "Gasto", null),
                descripcion,
                monto,
                LocalDateTime.of(2026, 11, 15, 10, 0)
        );
    }

    private PresupuestoResponse presupuesto() {
        return new PresupuestoResponse(
                1,
                10,
                100,
                LocalDateTime.of(2026, 10, 1, 0, 0),
                LocalDateTime.of(2026, 12, 31, 23, 59)
        );
    }
}
