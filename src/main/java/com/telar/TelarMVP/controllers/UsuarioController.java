package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.ActualizarUsuarioRequest;
import com.telar.TelarMVP.dto.CrearUsuarioRequest;
import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.exceptions.ApiExceptionHandler;
import com.telar.TelarMVP.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;


@Tag(name= "Usuarios controller",
description= "Endpoints para el manejo de los usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Operation(summary = "Listar todos los usuarios que hay en la base de datos")
    @GetMapping
    public List<Usuario> listar() {
        return usuarioService.listar();
    }

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
    public Usuario buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id);
    }

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
    public ResponseEntity<Usuario> crear(@Valid @RequestBody CrearUsuarioRequest request) {
        Usuario usuario = usuarioService.crear(request.nombre(), request.email(), request.password());
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.id())).body(usuario);
    }

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
    public Usuario actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarUsuarioRequest request
    ) {
        return usuarioService.actualizar(id, request.nombre(), request.email());
    }

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
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
