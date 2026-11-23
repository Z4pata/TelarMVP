package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.CrearPresupuestoRequest;
import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.Presupuesto;
import com.telar.TelarMVP.interfaces.service.JwtServiceInterface;
import com.telar.TelarMVP.interfaces.service.PresupuestoServiceInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Controlador de presupuestos",
description = "Endpoints para el manejo de presupuestos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/presupuestos")
public class PresupuestoController {

    @Autowired
    private PresupuestoServiceInterface presupuestoService;
    @Autowired
    private JwtServiceInterface jwtService;


    @PostMapping
    public Presupuesto crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CrearPresupuestoRequest request
    ) {
        return presupuestoService.crear(jwtService.obtenerUsuarioId(jwt), request);
    }

    @GetMapping("/actual")
    public PresupuestoResponse buscarPorUsuario(@AuthenticationPrincipal Jwt jwt) {
        return presupuestoService.buscarActualPorUsuario(jwtService.obtenerUsuarioId(jwt));
    }


}
