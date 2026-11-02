package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.CuentaRequest;
import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.entities.TipoCuenta;
import com.telar.TelarMVP.interfaces.Api.CuentaApiInterface;
import com.telar.TelarMVP.interfaces.service.CuentaServiceInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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

    private final CuentaServiceInterface cuentaService;

    public CuentaController(CuentaServiceInterface cuentaService) {
        this.cuentaService = cuentaService;
    }

    @Override
    public ResponseEntity<Cuenta> crear(@Valid @RequestBody CuentaRequest request) {
        Cuenta cuenta = cuentaService.insertar(convertir(request));
        return ResponseEntity.created(URI.create("/api/cuentas/" + cuenta.getId()))
                .body(cuenta);
    }

    @Override
    public ResponseEntity<List<Cuenta>> listar() {
        return ResponseEntity.ok(cuentaService.listar());
    }

    @Override
    public ResponseEntity<List<Cuenta>> listarPorUsuarioId(@PathVariable("usuarioId") Integer usuarioId) {
        return ResponseEntity.ok(cuentaService.listarPorUsuarioId(usuarioId));
    }

    @Override
    public ResponseEntity<Cuenta> findById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(cuentaService.findById(id));
    }

    @Override
    public ResponseEntity<Cuenta> actualizar(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CuentaRequest request
    ) {
        return ResponseEntity.ok(cuentaService.actualizar(id, convertir(request)));
    }

    @Override
    public ResponseEntity<Void> eliminar(@PathVariable("id") Integer id) {
        cuentaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    private Cuenta convertir(CuentaRequest request) {
        return new Cuenta(
                null,
                request.getUsuarioId(),
                new TipoCuenta(request.getTipoCuentaId(), null, null),
                request.getEntidad(),
                request.getDescripcion()
        );
    }
}
