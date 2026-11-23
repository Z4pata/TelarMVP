package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.CuentaRequest;
import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.entities.TipoCuenta;
import com.telar.TelarMVP.interfaces.Api.CuentaApiInterface;
import com.telar.TelarMVP.interfaces.service.CuentaServiceInterface;
import com.telar.TelarMVP.interfaces.service.JwtServiceInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Cuenta", description = "Endpoints para el manejo de cuentas financieras")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/cuentas")
public class CuentaController implements CuentaApiInterface {
    @Autowired
    private CuentaServiceInterface cuentaService;
    @Autowired
    private JwtServiceInterface jwtService;


    @Override
    public ResponseEntity<Cuenta> crear(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CuentaRequest request
    ) {
        Cuenta cuenta = cuentaService.insertar(convertir(request, jwtService.obtenerUsuarioId(jwt)));
        return ResponseEntity.created(URI.create("/api/cuentas/" + cuenta.getId()))
                .body(cuenta);
    }

    @Override
    public ResponseEntity<List<Cuenta>> listar() {
        return ResponseEntity.ok(cuentaService.listar());
    }

    @Override
    public ResponseEntity<List<Cuenta>> listarPorUsuarioId(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(cuentaService.listarPorUsuarioId(jwtService.obtenerUsuarioId(jwt)));
    }

    @Override
    public ResponseEntity<Cuenta> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }

    @Override
    public ResponseEntity<Cuenta> actualizar(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable("id") Integer id,
            @Valid @RequestBody CuentaRequest request
    ) {
        return ResponseEntity.ok(cuentaService.actualizar(id, convertir(request, jwtService.obtenerUsuarioId(jwt))));
    }

    @Override
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        cuentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Cuenta convertir(CuentaRequest request, Integer usuarioId) {
        return new Cuenta(
                null,
                usuarioId,
                new TipoCuenta(request.getTipoCuentaId(), null, null),
                request.getEntidad(),
                request.getDescripcion()
        );
    }
}
