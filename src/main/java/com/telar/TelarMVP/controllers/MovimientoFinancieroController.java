package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.MovimientoFinancieroRequest;
import com.telar.TelarMVP.entities.Categoria;
import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.entities.MovimientoFinanciero;
import com.telar.TelarMVP.entities.TipoMovimiento;
import com.telar.TelarMVP.interfaces.Api.MovimientoFinancieroApiInterface;
import com.telar.TelarMVP.interfaces.service.MovimientoFinancieroServiceInterface;
import com.telar.TelarMVP.interfaces.service.JwtServiceInterface;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Movimiento financiero", description = "Endpoints para crear movimientos financieros")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/movimientos")
public class MovimientoFinancieroController implements MovimientoFinancieroApiInterface {

    @Autowired
    private MovimientoFinancieroServiceInterface movimientoService;
    @Autowired
    private JwtServiceInterface jwtService;


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

    @Override
    public ResponseEntity<BigDecimal> totalActualPorUsuarioYTipoMovimiento(
            @AuthenticationPrincipal Jwt jwt,
            Integer tipoMovimientoId
    ) {
        return ResponseEntity.ok(
                movimientoService.totalActualPorUsuarioYTipoMovimiento(
                        jwtService.obtenerUsuarioId(jwt),
                        tipoMovimientoId
                )
        );
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
