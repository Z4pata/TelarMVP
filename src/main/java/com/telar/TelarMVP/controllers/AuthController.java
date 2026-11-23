package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.config.SessionCookie;
import com.telar.TelarMVP.dto.LoginRequest;
import com.telar.TelarMVP.dto.UsuarioCredenciales;
import com.telar.TelarMVP.interfaces.service.AuthServiceInterface;
import com.telar.TelarMVP.interfaces.service.JwtServiceInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Autenticacion",
        description = "Endpoints para el manejo de sesiones")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private JwtServiceInterface jwtService;
    @Autowired
    private AuthServiceInterface authService;
    @Autowired
    private SessionCookie sessionCookie;

    @PostMapping("/login")
    public ResponseEntity<Void> logIn(@Valid @RequestBody LoginRequest request) {
        UsuarioCredenciales usuario = authService.validar(request);
        String token = jwtService.crearToken(usuario.getId());

        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, sessionCookie.crear(token).toString())
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logOut() {
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, sessionCookie.eliminar().toString())
                .build();
    }
}
