package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.MovimientoFinancieroRequest;
import com.telar.TelarMVP.entities.Categoria;
import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.entities.MovimientoFinanciero;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.interfaces.Api.MovimientoFinancieroApiInterface;
import com.telar.TelarMVP.interfaces.service.MovimientoFinancieroInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@Tag(name = "Movimiento financiero", description = "Endpoints para crear movimientos financieros")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/movimientos")
public class MovimientoFinancieroController implements MovimientoFinancieroApiInterface {

    private final MovimientoFinancieroInterface movimientoService;

    public MovimientoFinancieroController(MovimientoFinancieroInterface movimientoService) {
        this.movimientoService = movimientoService;
    }

    @Override
    public ResponseEntity<MovimientoFinanciero> crear(
            @Valid @RequestBody MovimientoFinancieroRequest request
    ) {
        MovimientoFinanciero creado = movimientoService.crear(convertir(request));
        return ResponseEntity.created(URI.create("/api/movimientos/" + creado.getId()))
                .body(creado);
    }

    @Override
    public ResponseEntity<List<MovimientoFinanciero>> listarPorCuentaId(Integer cuentaId) {
        return ResponseEntity.ok(movimientoService.listarPorCuentaId(cuentaId));
    }

    private MovimientoFinanciero convertir(MovimientoFinancieroRequest request) {
        return new MovimientoFinanciero(
                null,
                new Categoria(request.getCategoriaId(), null, null),
                new Cuenta(request.getCuentaId(), null, null, null, null),
                new TipoMovimiento(request.getTipoMovimientoId(), null, null),
                request.getDescripcion(),
                request.getMonto(),
                request.getFecha()
        );
    }
}
