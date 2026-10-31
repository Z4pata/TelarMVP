package com.telar.TelarMVP.interfaces.Api;

import com.telar.TelarMVP.dto.CrearTipoCuentaRequest;
import com.telar.TelarMVP.entities.TipoCuenta;
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

import java.util.List;

public interface TipoCuentaApiInterface {

    @Operation(summary = "Crear un tipo de cuenta")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tipo de cuenta creado"),
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
    ResponseEntity<TipoCuenta> crear(@Valid @RequestBody CrearTipoCuentaRequest tipoCuenta);

    @Operation(summary = "Listar todos los tipos de cuenta")
    @ApiResponse(responseCode = "200", description = "Lista de tipos de cuenta")
    @GetMapping
    ResponseEntity<List<TipoCuenta>> listar();

    @Operation(summary = "Encontrar un tipo de cuenta por su nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de cuenta encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el tipo de cuenta",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("nombre/{nombre}")
    ResponseEntity<TipoCuenta> findByNombre(@PathVariable("nombre") String nombre);

    @Operation(summary = "Encontrar un tipo de cuenta por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tipo de cuenta encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el tipo de cuenta",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<TipoCuenta> findById(@PathVariable("id") Integer id);
}
