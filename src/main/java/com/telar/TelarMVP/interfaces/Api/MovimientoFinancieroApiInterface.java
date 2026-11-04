package com.telar.TelarMVP.interfaces.Api;

import com.telar.TelarMVP.dto.MovimientoFinancieroRequest;
import com.telar.TelarMVP.entities.MovimientoFinanciero;
import com.telar.TelarMVP.exceptions.ApiExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.List;

public interface MovimientoFinancieroApiInterface {

    @Operation(summary = "Crear un movimiento financiero")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Movimiento financiero creado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos o tipo de movimiento no soportado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la cuenta, categoría o tipo indicado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "La operación viola una restricción de la base de datos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @PostMapping
    ResponseEntity<MovimientoFinanciero> crear(@Valid @RequestBody MovimientoFinancieroRequest request);

    @Operation(summary = "Listar movimientos financieros por cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de movimientos de la cuenta"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la cuenta indicada",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/cuenta/{cuentaId}")
    ResponseEntity<List<MovimientoFinanciero>> listarPorCuentaId(
            @PathVariable("cuentaId") Integer cuentaId
    );

    @Operation(summary = "Consultar el total del periodo actual por usuario y tipo de movimiento")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Total acumulado durante el periodo del presupuesto actual"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el usuario, el tipo de movimiento o un presupuesto actual",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/usuario/{usuarioId}/total/{tipoMovimientoId}")
    ResponseEntity<BigDecimal> totalActualPorUsuarioYTipoMovimiento(
            @PathVariable("usuarioId") Integer usuarioId,
            @PathVariable("tipoMovimientoId") Integer tipoMovimientoId
    );
}
