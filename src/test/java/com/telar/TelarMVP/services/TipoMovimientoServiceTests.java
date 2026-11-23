package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.TipoMovimientoRequest;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.exceptions.ConflictoExcepcion;
import com.telar.TelarMVP.repositories.TipoMovimientoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TipoMovimientoServiceTests {

    @Mock
    private TipoMovimientoRepository tipoMovimientoRepository;

    @InjectMocks
    private TipoMovimientoService tipoMovimientoService;

    @Test
    void listarTipoMovimientos() {
        List<TipoMovimiento> lista = List.of(
                new TipoMovimiento(1, "Ingreso", "Cuando gano plata"),
                new TipoMovimiento(2, "Gasto", "Cuando me gasto la plata")
        );
        Mockito.when(tipoMovimientoRepository.findAll()).thenReturn(lista);

        List<TipoMovimiento> result = tipoMovimientoService.listar();
        assertNotNull(result);
        assertEquals(result.size(), lista.size());

        verify(tipoMovimientoRepository, times(1)).findAll();

    }

    @Test
    void listarTipoMovimientosVacio() {
        List<TipoMovimiento> lista = List.of();
        Mockito.when(tipoMovimientoRepository.findAll()).thenReturn(lista);

        List<TipoMovimiento> result = tipoMovimientoService.listar();
        assertEquals(result, lista);

        verify(tipoMovimientoRepository, times(1)).findAll();

    }

    @Test
    void encontrarTipoMovimientoPorNombre() {
        String nombre = "Ingreso";
        TipoMovimiento tipo = new TipoMovimiento(1, "Ingreso", "Cuando gano plata");

        when(tipoMovimientoRepository.findByNombre(nombre)).thenReturn(Optional.of(tipo));

        TipoMovimiento result = tipoMovimientoService.findByNombre(nombre);
        assertNotNull(result);
        assertEquals(result, tipo);
        verify(tipoMovimientoRepository, times(1)).findByNombre(nombre);
    }

    @Test
    void encontrarTipoMovimientoPorNombre_retornaNoEncontrado() {
        String nombre = "Ingreso";
        when(tipoMovimientoRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> tipoMovimientoService.findByNombre(nombre));
        verify(tipoMovimientoRepository, times(1)).findByNombre(nombre);
    }

    @Test
    void encontrarTipoMovimientoPorId() {
        Integer id = 1;
        TipoMovimiento tipo = new TipoMovimiento(1, "Ingreso", "Cuando gano plata");

        when(tipoMovimientoRepository.findById(id)).thenReturn(Optional.of(tipo));

        TipoMovimiento result = tipoMovimientoService.findById(id);
        assertNotNull(result);
        assertEquals(result, tipo);
        verify(tipoMovimientoRepository, times(1)).findById(id);
    }

    @Test
    void encontrarTipoMovimientoPorId_retornaNoEncontrado() {
        Integer id = 1;
        when(tipoMovimientoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> tipoMovimientoService.findById(id));
        verify(tipoMovimientoRepository, times(1)).findById(id);
    }

    private TipoMovimientoRequest request(String nombre) {
        TipoMovimientoRequest request = new TipoMovimientoRequest();
        request.setNombre(nombre);
        return request;
    }

    @Test
    void actualizarTipoMovimiento() {
        TipoMovimiento tipo = new TipoMovimiento(3, "Ahorro", null);
        when(tipoMovimientoRepository.findById(3)).thenReturn(Optional.of(tipo));
        when(tipoMovimientoRepository.save(tipo)).thenReturn(tipo);

        TipoMovimiento result = tipoMovimientoService.actualizar(3, request("Inversion"));

        assertEquals("Inversion", result.getNombre());
        verify(tipoMovimientoRepository).save(tipo);
    }

    @Test
    void actualizarTipoMovimiento_rechazaTipoProtegido() {
        when(tipoMovimientoRepository.findById(1))
                .thenReturn(Optional.of(new TipoMovimiento(1, "Ingreso", null)));

        assertThrows(ConflictoExcepcion.class,
                () -> tipoMovimientoService.actualizar(1, request("Salario")));
        verify(tipoMovimientoRepository, never()).save(any(TipoMovimiento.class));
    }

    @Test
    void actualizarTipoMovimiento_rechazaRenombrarComoProtegido() {
        when(tipoMovimientoRepository.findById(3))
                .thenReturn(Optional.of(new TipoMovimiento(3, "Ahorro", null)));

        assertThrows(ConflictoExcepcion.class,
                () -> tipoMovimientoService.actualizar(3, request("egreso")));
        verify(tipoMovimientoRepository, never()).save(any(TipoMovimiento.class));
    }

    @Test
    void eliminarTipoMovimiento_rechazaTipoProtegido() {
        when(tipoMovimientoRepository.findById(2))
                .thenReturn(Optional.of(new TipoMovimiento(2, "Egreso", null)));

        assertThrows(ConflictoExcepcion.class, () -> tipoMovimientoService.eliminar(2));
        verify(tipoMovimientoRepository, never()).delete(any(TipoMovimiento.class));
    }

    @Test
    void eliminarTipoMovimiento() {
        TipoMovimiento tipo = new TipoMovimiento(3, "Ahorro", null);
        when(tipoMovimientoRepository.findById(3)).thenReturn(Optional.of(tipo));

        tipoMovimientoService.eliminar(3);

        verify(tipoMovimientoRepository).delete(tipo);
    }
}
