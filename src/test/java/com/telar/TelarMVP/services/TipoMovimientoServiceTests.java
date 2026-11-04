package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.TipoMovimientoRequest;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.repositories.TipoMovimientoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
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

    @Test
    void insertarTipoMovimiento() {
        TipoMovimientoRequest request = new TipoMovimientoRequest();
        request.setNombre("Ingreso");
        request.setDescripcion("Cuando gano plata");
        TipoMovimiento guardado = new TipoMovimiento(1, "Ingreso", "Cuando gano plata");
        when(tipoMovimientoRepository.save(any(TipoMovimiento.class))).thenReturn(guardado);

        TipoMovimiento result = tipoMovimientoService.insertar(request);

        ArgumentCaptor<TipoMovimiento> captor = ArgumentCaptor.forClass(TipoMovimiento.class);
        verify(tipoMovimientoRepository).save(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("Ingreso", captor.getValue().getNombre());
        assertEquals("Cuando gano plata", captor.getValue().getDescripcion());
        assertEquals(guardado, result);
    }

    @Test
    void insertarTipoMovimiento_retornaErrorCuandoViolaRestriccion() {
        TipoMovimientoRequest request = new TipoMovimientoRequest();
        request.setNombre("Ingreso");
        request.setDescripcion("Cuando gano plata");
        when(tipoMovimientoRepository.save(any(TipoMovimiento.class)))
                .thenThrow(new DataIntegrityViolationException("Restriccion de base de datos"));

        assertThrows(DataIntegrityViolationException.class, () -> tipoMovimientoService.insertar(request));
        verify(tipoMovimientoRepository).save(any(TipoMovimiento.class));
    }

    @Test
    void actualizarTipoMovimiento() {
        Integer id = 1;
        TipoMovimiento existente = new TipoMovimiento(id, "Ingreso", "Descripcion anterior");
        TipoMovimientoRequest request = new TipoMovimientoRequest();
        request.setNombre("Gasto");
        request.setDescripcion("Descripcion actualizada");
        when(tipoMovimientoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(tipoMovimientoRepository.save(existente)).thenReturn(existente);

        TipoMovimiento result = tipoMovimientoService.actualizar(id, request);

        assertEquals(id, result.getId());
        assertEquals("Gasto", result.getNombre());
        assertEquals("Descripcion actualizada", result.getDescripcion());
        verify(tipoMovimientoRepository).findById(id);
        verify(tipoMovimientoRepository).save(existente);
    }

    @Test
    void actualizarTipoMovimiento_retornaErrorCuandoNoExiste() {
        Integer id = 99;
        TipoMovimientoRequest request = new TipoMovimientoRequest();
        request.setNombre("Gasto");
        request.setDescripcion("Descripcion");
        when(tipoMovimientoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> tipoMovimientoService.actualizar(id, request));
        verify(tipoMovimientoRepository).findById(id);
        verify(tipoMovimientoRepository, never()).save(any(TipoMovimiento.class));
    }

    @Test
    void actualizarTipoMovimiento_retornaErrorCuandoViolaRestriccion() {
        Integer id = 1;
        TipoMovimiento existente = new TipoMovimiento(id, "Ingreso", "Descripcion anterior");
        TipoMovimientoRequest request = new TipoMovimientoRequest();
        request.setNombre("Gasto");
        request.setDescripcion("Descripcion actualizada");
        when(tipoMovimientoRepository.findById(id)).thenReturn(Optional.of(existente));
        when(tipoMovimientoRepository.save(existente))
                .thenThrow(new DataIntegrityViolationException("Restriccion de base de datos"));

        assertThrows(DataIntegrityViolationException.class, () -> tipoMovimientoService.actualizar(id, request));
        verify(tipoMovimientoRepository).save(existente);
    }

    @Test
    void eliminarTipoMovimiento() {
        Integer id = 1;
        TipoMovimiento existente = new TipoMovimiento(id, "Ingreso", "Cuando gano plata");
        when(tipoMovimientoRepository.findById(id)).thenReturn(Optional.of(existente));

        tipoMovimientoService.eliminar(id);

        verify(tipoMovimientoRepository).findById(id);
        verify(tipoMovimientoRepository).delete(existente);
    }

    @Test
    void eliminarTipoMovimiento_retornaErrorCuandoNoExiste() {
        Integer id = 99;
        when(tipoMovimientoRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> tipoMovimientoService.eliminar(id));
        verify(tipoMovimientoRepository).findById(id);
        verify(tipoMovimientoRepository, never()).delete(any(TipoMovimiento.class));
    }
}
