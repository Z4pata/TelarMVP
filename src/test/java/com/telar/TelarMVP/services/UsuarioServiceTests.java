package com.telar.TelarMVP.services;

import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.exceptions.UsuarioNoEncontradoException;
import com.telar.TelarMVP.repositories.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTests {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void cifraLaContrasenaAntesDeCrearElUsuario() {
        Usuario esperado = new Usuario(1, "Laura", "laura@example.com");
        when(usuarioRepository.crear(anyString(), anyString(), anyString())).thenReturn(esperado);

        Usuario resultado = usuarioService.crear("Laura", "laura@example.com", "ClaveSegura123");

        ArgumentCaptor<String> hash = ArgumentCaptor.forClass(String.class);
        verify(usuarioRepository).crear(
                org.mockito.ArgumentMatchers.eq("Laura"),
                org.mockito.ArgumentMatchers.eq("laura@example.com"),
                hash.capture()
        );
        assertTrue(new BCryptPasswordEncoder().matches("ClaveSegura123", hash.getValue()));
        assertEquals(esperado, resultado);
    }

    @Test
    void informaCuandoElUsuarioNoExiste() {
        when(usuarioRepository.buscarPorId(99)).thenReturn(Optional.empty());

        assertThrows(UsuarioNoEncontradoException.class, () -> usuarioService.buscarPorId(99));
    }
}
