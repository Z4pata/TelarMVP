package com.telar.TelarMVP.interfaces.Api;

import com.telar.TelarMVP.dto.ActualizarUsuarioRequest;
import com.telar.TelarMVP.dto.CrearUsuarioRequest;
import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.exceptions.ApiExceptionHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface UsuarioApiInterface {

    @Operation(summary = "Listar todos los usuarios que hay en la base de datos")
    @GetMapping
    List<Usuario> listar();

    @Operation(summary = "Encontrar un usuario por su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el usuario",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/{id}")
    Usuario buscarPorId(@PathVariable Integer id);

    @Operation(summary = "Crear un usuario")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuario creado"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no validos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Dato duplicado",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @PostMapping
    ResponseEntity<Usuario> crear(
            @Valid @RequestBody CrearUsuarioRequest request
    );

    @Operation(summary = "Actualizar por completo un usuario ingresando su id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el usuario",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no validos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @PutMapping("/{id}")
    Usuario actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarUsuarioRequest request
    );

    @Operation(summary = "Eliminar un usuario ingresando su id")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Usuario eliminado"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe el usuario",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@PathVariable Integer id);
}
