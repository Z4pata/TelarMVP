package com.telar.TelarMVP.services;

import com.telar.TelarMVP.entities.Categoria;
import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.entities.MovimientoFinanciero;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.exceptions.CuentaNoEncontradaException;
import com.telar.TelarMVP.exceptions.TipoMovimientoNoSoportadoException;
import com.telar.TelarMVP.interfaces.repository.PresupuestoRepositoryInterface;
import com.telar.TelarMVP.interfaces.repository.UsuarioRepositoryInterface;
import com.telar.TelarMVP.interfaces.service.PresupuestoServiceInterface;
import com.telar.TelarMVP.repositories.MovimientoFinancieroRepository;
import com.telar.TelarMVP.repositories.CategoriaRepository;
import com.telar.TelarMVP.repositories.CuentaRepository;
import com.telar.TelarMVP.repositories.TipoMovimientoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovimientoFinancieroServiceTests {

    @Mock
    private MovimientoFinancieroRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private TipoMovimientoRepository tipoMovimientoRepository;

    @Mock
    private PresupuestoRepositoryInterface presupuestoRepository;

    @Mock
    private UsuarioRepositoryInterface usuarioRepository;

    @Mock
    private PresupuestoServiceInterface presupuestoService;

    private MovimientoFinancieroService movimientoService;

    @BeforeEach
    void setUp() {
        ProcesadorIngreso procesadorIngreso = new ProcesadorIngreso();
        ProcesadorEgreso procesadorEgreso = new ProcesadorEgreso(movimientoRepository, presupuestoRepository);
        movimientoService = new MovimientoFinancieroService(
                List.of(procesadorIngreso, procesadorEgreso)
        );
        ReflectionTestUtils.setField(movimientoService, "movimientoRepository", movimientoRepository);
        ReflectionTestUtils.setField(movimientoService, "cuentaRepository", cuentaRepository);
        ReflectionTestUtils.setField(movimientoService, "categoriaRepository", categoriaRepository);
        ReflectionTestUtils.setField(movimientoService, "tipoMovimientoRepository", tipoMovimientoRepository);
        ReflectionTestUtils.setField(movimientoService, "usuarioRepository", usuarioRepository);
        ReflectionTestUtils.setField(movimientoService, "presupuestoService", presupuestoService);
    }

    @Test
    void crearMovimientoEgresoProcesaPresupuestoYGuarda() {
        Cuenta cuenta = new Cuenta(3, 10, null, "Banco", null);
        Categoria categoria = new Categoria(4, "Alimentación", null);
        TipoMovimiento tipo = new TipoMovimiento(6, ProcesadorEgreso.CODIGO_TIPO, null);
        MovimientoFinanciero request = movimiento(cuenta, categoria, tipo, new BigDecimal("25.00"));
        when(cuentaRepository.findById(3)).thenReturn(Optional.of(cuenta));
        when(categoriaRepository.findById(4)).thenReturn(Optional.of(categoria));
        when(tipoMovimientoRepository.findById(6)).thenReturn(Optional.of(tipo));
        when(presupuestoRepository.existeActivo(10)).thenReturn(false);
        when(movimientoRepository.save(any(MovimientoFinanciero.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovimientoFinanciero resultado = movimientoService.crear(request);

        assertEquals(request, resultado);
        verify(presupuestoRepository).existeActivo(10);
        verify(movimientoRepository).save(request);
    }

    @Test
    void crearMovimientoIngresoValidaRelacionesYGuarda() {
        Cuenta cuenta = new Cuenta(3, 10, null, "Banco", null);
        Categoria categoria = new Categoria(4, "Salario", null);
        TipoMovimiento tipo = new TipoMovimiento(5, "Ingreso", null);
        MovimientoFinanciero request = movimiento(cuenta, categoria, tipo, new BigDecimal("100.00"));
        when(cuentaRepository.findById(3)).thenReturn(Optional.of(cuenta));
        when(categoriaRepository.findById(4)).thenReturn(Optional.of(categoria));
        when(tipoMovimientoRepository.findById(5)).thenReturn(Optional.of(tipo));
        when(movimientoRepository.save(any(MovimientoFinanciero.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        MovimientoFinanciero resultado = movimientoService.crear(request);

        assertEquals(request, resultado);
        verify(cuentaRepository).findById(3);
        verify(categoriaRepository).findById(4);
        verify(tipoMovimientoRepository).findById(5);
        verify(movimientoRepository).save(request);
    }

    @Test
    void listarPorCuentaIdDevuelveMovimientosDeLaCuenta() {
        Cuenta cuenta = new Cuenta(3, 10, null, "Banco", null);
        List<MovimientoFinanciero> movimientos = List.of(
                movimiento(cuenta, new Categoria(4, "Alimentación", null),
                        new TipoMovimiento(6, ProcesadorEgreso.CODIGO_TIPO, null), new BigDecimal("25.00"))
        );
        when(cuentaRepository.findById(3)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findAllByCuenta_Id(3)).thenReturn(movimientos);

        assertEquals(movimientos, movimientoService.listarPorCuentaId(3));
        verify(cuentaRepository).findById(3);
        verify(movimientoRepository).findAllByCuenta_Id(3);
    }

    @Test
    void listarPorCuentaIdFallaSiLaCuentaNoExiste() {
        when(cuentaRepository.findById(99)).thenReturn(Optional.empty());

        assertThrows(CuentaNoEncontradaException.class, () -> movimientoService.listarPorCuentaId(99));
        verify(movimientoRepository, never()).findAllByCuenta_Id(99);
    }

    @Test
    void totalActualPorUsuarioYTipoMovimientoDevuelveSumaDelPeriodoDelPresupuesto() {
        LocalDateTime inicio = LocalDateTime.of(2026, 11, 1, 0, 0);
        LocalDateTime fin = LocalDateTime.of(2026, 11, 30, 23, 59);
        BigDecimal total = new BigDecimal("125.50");
        when(usuarioRepository.buscarPorId(10)).thenReturn(Optional.of(new Usuario(10, "Usuario", "u@example.com")));
        when(tipoMovimientoRepository.findById(5))
                .thenReturn(Optional.of(new TipoMovimiento(5, "Ingreso", null)));
        when(presupuestoService.buscarActualPorUsuario(10))
                .thenReturn(new PresupuestoResponse(1, 10, 1000, inicio, fin));
        when(movimientoRepository.sumarPorUsuarioTipoYPeriodo(10, "Ingreso", inicio, fin)).thenReturn(total);

        assertEquals(total, movimientoService.totalActualPorUsuarioYTipoMovimiento(10, 5));
        verify(movimientoRepository).sumarPorUsuarioTipoYPeriodo(10, "Ingreso", inicio, fin);
    }

    private MovimientoFinanciero movimiento(
            Cuenta cuenta,
            Categoria categoria,
            TipoMovimiento tipo,
            BigDecimal monto
    ) {
        return new MovimientoFinanciero(null, categoria, cuenta, tipo, "Descripción", monto,
                LocalDateTime.of(2026, 11, 1, 12, 0));
    }
}
