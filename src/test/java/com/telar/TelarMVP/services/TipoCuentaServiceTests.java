package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.CrearTipoCuentaRequest;
import com.telar.TelarMVP.entities.TipoCuenta;
import com.telar.TelarMVP.repositories.TipoCuentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoCuentaServiceTests {

    @Mock
    private TipoCuentaRepository tipoCuentaRepository;

    @InjectMocks
    private TipoCuentaService tipoCuentaService;

    @Test
    void listarTiposCuenta() {
        List<TipoCuenta> lista = List.of(
                new TipoCuenta(1, "Ahorros", "Cuenta de ahorros"),
                new TipoCuenta(2, "Corriente", "Cuenta corriente")
        );
        when(tipoCuentaRepository.findAll()).thenReturn(lista);

        List<TipoCuenta> result = tipoCuentaService.listar();

        assertNotNull(result);
        assertEquals(lista.size(), result.size());
        assertEquals(lista, result);
        verify(tipoCuentaRepository).findAll();
    }

    @Test
    void listarTiposCuentaVacio() {
        List<TipoCuenta> lista = List.of();
        when(tipoCuentaRepository.findAll()).thenReturn(lista);

        List<TipoCuenta> result = tipoCuentaService.listar();

        assertEquals(lista, result);
        verify(tipoCuentaRepository).findAll();
    }

    @Test
    void encontrarTipoCuentaPorNombre() {
        String nombre = "Ahorros";
        TipoCuenta tipoCuenta = new TipoCuenta(1, nombre, "Cuenta de ahorros");
        when(tipoCuentaRepository.findByNombre(nombre)).thenReturn(Optional.of(tipoCuenta));

        TipoCuenta result = tipoCuentaService.findByNombre(nombre);

        assertNotNull(result);
        assertEquals(tipoCuenta, result);
        verify(tipoCuentaRepository).findByNombre(nombre);
    }

    @Test
    void encontrarTipoCuentaPorNombre_retornaNoEncontrado() {
        String nombre = "Ahorros";
        when(tipoCuentaRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> tipoCuentaService.findByNombre(nombre));
        verify(tipoCuentaRepository).findByNombre(nombre);
    }

    @Test
    void encontrarTipoCuentaPorId() {
        Integer id = 1;
        TipoCuenta tipoCuenta = new TipoCuenta(id, "Ahorros", "Cuenta de ahorros");
        when(tipoCuentaRepository.findById(id)).thenReturn(Optional.of(tipoCuenta));

        TipoCuenta result = tipoCuentaService.findById(id);

        assertNotNull(result);
        assertEquals(tipoCuenta, result);
        verify(tipoCuentaRepository).findById(id);
    }

    @Test
    void encontrarTipoCuentaPorId_retornaNoEncontrado() {
        Integer id = 99;
        when(tipoCuentaRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EmptyResultDataAccessException.class, () -> tipoCuentaService.findById(id));
        verify(tipoCuentaRepository).findById(id);
    }

    @Test
    void insertarTipoCuenta() {
        CrearTipoCuentaRequest request = new CrearTipoCuentaRequest();
        request.setNombre("Ahorros");
        request.setDescripcion("Cuenta de ahorros");
        TipoCuenta guardado = new TipoCuenta(1, "Ahorros", "Cuenta de ahorros");
        when(tipoCuentaRepository.save(any(TipoCuenta.class))).thenReturn(guardado);

        TipoCuenta result = tipoCuentaService.insertar(request);

        ArgumentCaptor<TipoCuenta> captor = ArgumentCaptor.forClass(TipoCuenta.class);
        verify(tipoCuentaRepository).save(captor.capture());
        assertNull(captor.getValue().getId());
        assertEquals("Ahorros", captor.getValue().getNombre());
        assertEquals("Cuenta de ahorros", captor.getValue().getDescripcion());
        assertEquals(guardado, result);
    }

    @Test
    void insertarTipoCuenta_retornaErrorCuandoViolaRestriccion() {
        CrearTipoCuentaRequest request = new CrearTipoCuentaRequest();
        request.setNombre("Ahorros");
        request.setDescripcion("Cuenta de ahorros");
        when(tipoCuentaRepository.save(any(TipoCuenta.class)))
                .thenThrow(new DataIntegrityViolationException("Restriccion de base de datos"));

        assertThrows(DataIntegrityViolationException.class, () -> tipoCuentaService.insertar(request));
        verify(tipoCuentaRepository).save(any(TipoCuenta.class));
    }
}
