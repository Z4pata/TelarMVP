package com.telar.TelarMVP.interfaces.Api;

import com.telar.TelarMVP.dto.TipoMovimientoRequest;
import com.telar.TelarMVP.entities.TipoMovimiento;
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

public interface TipoMovimientoApiInterface {

    @Operation(summary = "Crear un tipo de movimiento")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de movimiento creado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "La operación viola una restricción de la base de datos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @PostMapping
    ResponseEntity<TipoMovimiento> crear(@Valid @RequestBody TipoMovimientoRequest request);

    @Operation(summary = "Listar todos los tipos de movimiento")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de movimiento")
    @GetMapping
    ResponseEntity<List<TipoMovimiento>> listar();

    @Operation(summary = "Encontrar un tipo de movimiento por su nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de movimiento encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el tipo de movimiento",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/nombre/{nombre}")
    ResponseEntity<TipoMovimiento> findByNombre(@PathVariable("nombre") String nombre);

    @Operation(summary = "Encontrar un tipo de movimiento por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de movimiento encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el tipo de movimiento",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<TipoMovimiento> findById(@PathVariable("id") Integer id);

    @Operation(summary = "Actualizar un tipo de movimiento por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de movimiento actualizado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el tipo de movimiento",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "La operación viola una restricción de la base de datos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @PutMapping("/{id}")
    ResponseEntity<TipoMovimiento> actualizar(
            @PathVariable("id") Integer id,
            @Valid @RequestBody TipoMovimientoRequest request
    );

    @Operation(summary = "Eliminar un tipo de movimiento por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tipo de movimiento eliminado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el tipo de movimiento",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@PathVariable("id") Integer id);
}
