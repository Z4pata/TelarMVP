package com.telar.TelarMVP.services;

import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.entities.TipoCuenta;
import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.exceptions.TipoCuentaNoEncontradaException;
import com.telar.TelarMVP.exceptions.UsuarioNoEncontradoException;
import com.telar.TelarMVP.repositories.CuentaRepository;
import com.telar.TelarMVP.interfaces.repository.UsuarioRepositoryInterface;
import com.telar.TelarMVP.repositories.TipoCuentaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTests {

    @Mock
    private CuentaRepository cuentaRepository;

    @Mock
    private UsuarioRepositoryInterface usuarioRepository;

    @Mock
    private TipoCuentaRepository tipoCuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    @Test
    void insertarVerificaLasRelacionesYGuardaLaCuenta() {
        TipoCuenta tipoCuenta = new TipoCuenta(2, "Ahorros", null);
        Cuenta cuenta = new Cuenta(null, 1, new TipoCuenta(2, null, null), "Banco", null);
        when(usuarioRepository.buscarPorId(1)).thenReturn(Optional.of(new Usuario(1, "Ana", "ana@example.com")));
        when(tipoCuentaRepository.findById(2)).thenReturn(Optional.of(tipoCuenta));
        when(cuentaRepository.save(cuenta)).thenReturn(cuenta);

        Cuenta resultado = cuentaService.insertar(cuenta);

        assertEquals(cuenta, resultado);
        assertEquals(tipoCuenta, cuenta.getTipoCuenta());
        verify(usuarioRepository).buscarPorId(1);
        verify(tipoCuentaRepository).findById(2);
        verify(cuentaRepository).save(cuenta);
    }

    @Test
    void insertarFallaCuandoElUsuarioNoExiste() {
        Cuenta cuenta = new Cuenta(null, 1, new TipoCuenta(2, null, null), "Banco", null);
        when(usuarioRepository.buscarPorId(1)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> cuentaService.insertar(cuenta));
        verify(tipoCuentaRepository, never()).findById(2);
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void insertarFallaCuandoElTipoCuentaNoExiste() {
        Cuenta cuenta = new Cuenta(null, 1, new TipoCuenta(2, null, null), "Banco", null);
        when(usuarioRepository.buscarPorId(1)).thenReturn(Optional.of(new Usuario(1, "Ana", "ana@example.com")));
        when(tipoCuentaRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(TipoCuentaNoEncontradaException.class, () -> cuentaService.insertar(cuenta));
        verify(cuentaRepository, never()).save(any(Cuenta.class));
    }

    @Test
    void listarPorUsuarioIdDevuelveLasCuentasDelUsuario() {
        List<Cuenta> cuentas = List.of(
                new Cuenta(1, 7, new TipoCuenta(2, "Ahorros", null), "Banco", null)
        );
        when(usuarioRepository.buscarPorId(7)).thenReturn(Optional.of(new Usuario(7, "Ana", "ana@example.com")));
        when(cuentaRepository.findAllByUsuarioId(7)).thenReturn(cuentas);

        assertEquals(cuentas, cuentaService.listarPorUsuarioId(7));
        verify(cuentaRepository).findAllByUsuarioId(7);
    }

    @Test
    void listarPorUsuarioIdFallaSiElUsuarioNoExiste() {
        when(usuarioRepository.buscarPorId(99)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> cuentaService.listarPorUsuarioId(99));
        verify(cuentaRepository, never()).findAllByUsuarioId(99);
    }
}
