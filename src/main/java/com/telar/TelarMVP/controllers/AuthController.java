package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.LoginRequest;
import com.telar.TelarMVP.dto.UsuarioCredenciales;
import com.telar.TelarMVP.interfaces.service.AuthServiceInterface;
import com.telar.TelarMVP.interfaces.service.JwtServiceInterface;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/login")
    public String logIn(LoginRequest request) {
        UsuarioCredenciales usuario = authService.validar(request);
        return jwtService.crearToken(usuario.getId());

    }
}
