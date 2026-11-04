package com.telar.TelarMVP.services;

import com.telar.TelarMVP.dto.CrearPresupuestoRequest;
import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.Presupuesto;
import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.exceptions.ConflictoExcepcion;
import com.telar.TelarMVP.exceptions.PresupuestoNoEncontradoException;
import com.telar.TelarMVP.exceptions.UsuarioNoEncontradoException;
import com.telar.TelarMVP.interfaces.repository.PresupuestoRepositoryInterface;
import com.telar.TelarMVP.interfaces.repository.UsuarioRepositoryInterface;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PresupuestoServiceTests {

    @Mock
    private PresupuestoRepositoryInterface presupuestoRepository;

    @Mock
    private UsuarioRepositoryInterface usuarioRepository;

    @InjectMocks
    private PresupuestoService presupuestoService;

    @Test
    void crearPresupuesto() {
        Integer usuarioId = 1;
        Usuario usuario = new Usuario(usuarioId, "Laura", "laura@example.com");
        LocalDateTime fechaFin = LocalDateTime.now().plusDays(30);
        CrearPresupuestoRequest request = new CrearPresupuestoRequest();
        request.setUsuarioId(usuarioId);
        request.setMontoLimite(1500.0);
        request.setFechaFin(fechaFin);
        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(presupuestoRepository.existeActivo(usuarioId)).thenReturn(false);
        when(presupuestoRepository.crear(any(Presupuesto.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        LocalDateTime inicioPrueba = LocalDateTime.now();

        Presupuesto result = presupuestoService.crear(request);

        LocalDateTime finPrueba = LocalDateTime.now();
        ArgumentCaptor<Presupuesto> captor = ArgumentCaptor.forClass(Presupuesto.class);
        verify(presupuestoRepository).crear(captor.capture());
        assertEquals(usuario, captor.getValue().getUsuario());
        assertEquals(1500.0, captor.getValue().getMontoLimite());
        assertEquals(fechaFin, captor.getValue().getFechaFin());
        assertFalse(captor.getValue().getFechaInicio().isBefore(inicioPrueba));
        assertFalse(captor.getValue().getFechaInicio().isAfter(finPrueba));
        assertEquals(captor.getValue(), result);
        verify(usuarioRepository).buscarPorId(usuarioId);
        verify(presupuestoRepository).existeActivo(usuarioId);
    }

    @Test
    void crearPresupuesto_retornaErrorCuandoUsuarioNoExiste() {
        Integer usuarioId = 99;
        CrearPresupuestoRequest request = new CrearPresupuestoRequest();
        request.setUsuarioId(usuarioId);
        request.setMontoLimite(1500.0);
        request.setFechaFin(LocalDateTime.now().plusDays(30));
        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> presupuestoService.crear(request));
        verify(usuarioRepository).buscarPorId(usuarioId);
        verify(presupuestoRepository, never()).existeActivo(anyInt());
        verify(presupuestoRepository, never()).crear(any(Presupuesto.class));
    }

    @Test
    void crearPresupuesto_retornaErrorCuandoYaExisteUnoActivo() {
        Integer usuarioId = 1;
        Usuario usuario = new Usuario(usuarioId, "Laura", "laura@example.com");
        CrearPresupuestoRequest request = new CrearPresupuestoRequest();
        request.setUsuarioId(usuarioId);
        request.setMontoLimite(1500.0);
        request.setFechaFin(LocalDateTime.now().plusDays(30));
        when(usuarioRepository.buscarPorId(usuarioId)).thenReturn(Optional.of(usuario));
        when(presupuestoRepository.existeActivo(usuarioId)).thenReturn(true);

        assertThrows(ConflictoExcepcion.class, () -> presupuestoService.crear(request));
        verify(usuarioRepository).buscarPorId(usuarioId);
        verify(presupuestoRepository).existeActivo(usuarioId);
        verify(presupuestoRepository, never()).crear(any(Presupuesto.class));
    }

    @Test
    void buscarPresupuestoActualPorUsuario() {
        Integer usuarioId = 1;
        PresupuestoResponse esperado = new PresupuestoResponse(
                10,
                usuarioId,
                1500.0,
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(30)
        );
        when(presupuestoRepository.buscarActualPorUsuario(usuarioId)).thenReturn(esperado);

        PresupuestoResponse result = presupuestoService.buscarActualPorUsuario(usuarioId);

        assertNotNull(result);
        assertEquals(esperado, result);
        verify(presupuestoRepository).buscarActualPorUsuario(usuarioId);
    }

    @Test
    void buscarPresupuestoActualPorUsuario_retornaErrorCuandoNoExiste() {
        Integer usuarioId = 99;
        when(presupuestoRepository.buscarActualPorUsuario(usuarioId))
                .thenThrow(new PresupuestoNoEncontradoException(
                        "No se encontro un presupuesto actual para el usuario " + usuarioId));

        assertThrows(
                PresupuestoNoEncontradoException.class,
                () -> presupuestoService.buscarActualPorUsuario(usuarioId)
        );
        verify(presupuestoRepository).buscarActualPorUsuario(usuarioId);
    }
}
