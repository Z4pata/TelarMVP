package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.TipoMovimientoRequest;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.interfaces.Api.TipoMovimientoApiInterface;
import com.telar.TelarMVP.interfaces.service.TipoMovimientoServiceInterface;
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

@Tag(name = "TipoMovimiento", description = "Endpoints para el manejo de tipos de movimiento")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/tipos-movimiento")
public class TipoMovimientoController implements TipoMovimientoApiInterface {

    private final TipoMovimientoServiceInterface tipoMovimientoService;

    public TipoMovimientoController(TipoMovimientoServiceInterface tipoMovimientoService) {
        this.tipoMovimientoService = tipoMovimientoService;
    }

    @Override
    public ResponseEntity<TipoMovimiento> crear(@Valid @RequestBody TipoMovimientoRequest request) {
        TipoMovimiento tipoMovimiento = tipoMovimientoService.insertar(request);
        return ResponseEntity.created(URI.create("/api/tipos-movimiento/" + tipoMovimiento.getId()))
                .body(tipoMovimiento);
    }

    @Override
    public ResponseEntity<List<TipoMovimiento>> listar() {
        return ResponseEntity.ok(tipoMovimientoService.listar());
    }

    @Override
    public ResponseEntity<TipoMovimiento> findByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(tipoMovimientoService.findByNombre(nombre));
    }

    @Override
    public ResponseEntity<TipoMovimiento> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoMovimientoService.findById(id));
    }

    @Override
    public ResponseEntity<TipoMovimiento> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody TipoMovimientoRequest request
    ) {
        return ResponseEntity.ok(tipoMovimientoService.actualizar(id, request));
    }

    @Override
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        tipoMovimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
