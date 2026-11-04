package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.CrearPresupuestoRequest;
import com.telar.TelarMVP.dto.PresupuestoResponse;
import com.telar.TelarMVP.entities.Presupuesto;
import com.telar.TelarMVP.interfaces.service.PresupuestoServiceInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Controlador de presupuestos",
description = "Endpoints para el manejo de presupuestos")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/presupuestos")
public class PresupuestoController {

    @Autowired
    private PresupuestoServiceInterface presupuestoService;

    @PostMapping
    public Presupuesto crear(@Valid @RequestBody CrearPresupuestoRequest request){
        return presupuestoService.crear(request);
    }

    @GetMapping("/actual/{userId}")
    public PresupuestoResponse buscarPorUsuario(@PathVariable("userId") Integer userId){
        return presupuestoService.buscarActualPorUsuario(userId);
    }


}
