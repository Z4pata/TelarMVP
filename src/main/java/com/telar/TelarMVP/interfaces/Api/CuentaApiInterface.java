package com.telar.TelarMVP.interfaces.Api;

import com.telar.TelarMVP.dto.CuentaRequest;
import com.telar.TelarMVP.entities.Cuenta;
import com.telar.TelarMVP.exceptions.ApiExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface CuentaApiInterface {

    @Operation(summary = "Crear una cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Cuenta creada"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el usuario o el tipo de cuenta indicado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "La operación viola una restricción de la base de datos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @PostMapping
    ResponseEntity<Cuenta> crear(@Valid @RequestBody CuentaRequest request);

    @Operation(summary = "Listar todas las cuentas")
    @ApiResponse(responseCode = "200", description = "Lista de cuentas")
    @GetMapping
    ResponseEntity<List<Cuenta>> listar();

    @Operation(summary = "Listar las cuentas de un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de cuentas del usuario"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el usuario indicado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/usuario/{usuarioId}")
    ResponseEntity<List<Cuenta>> listarPorUsuarioId(@PathVariable("usuarioId") Integer usuarioId);

    @Operation(summary = "Encontrar una cuenta por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la cuenta",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<Cuenta> findById(@PathVariable("id") Integer id);

    @Operation(summary = "Actualizar una cuenta por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta actualizada"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la cuenta, el usuario o el tipo de cuenta indicado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "La operación viola una restricción de la base de datos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @PutMapping("/{id}")
    ResponseEntity<Cuenta> actualizar(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CuentaRequest request
    );

    @Operation(summary = "Eliminar una cuenta por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta eliminada"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la cuenta",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@PathVariable("id") Integer id);
}
