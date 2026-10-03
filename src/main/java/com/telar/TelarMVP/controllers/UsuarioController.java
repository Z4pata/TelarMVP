package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.ActualizarUsuarioRequest;
import com.telar.TelarMVP.dto.CrearUsuarioRequest;
import com.telar.TelarMVP.entities.Usuario;
import com.telar.TelarMVP.interfaces.Api.UsuarioApiInterface;
import com.telar.TelarMVP.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;


@Tag(name= "Usuarios controller",
description= "Endpoints para el manejo de los usuarios")
@RestController
@RequestMapping("/usuarios")
public class UsuarioController implements UsuarioApiInterface {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    public List<Usuario> listar() {
        return usuarioService.listar();
    }


    public Usuario buscarPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id);
    }


    public ResponseEntity<Usuario> crear(@Valid @RequestBody CrearUsuarioRequest request) {
        Usuario usuario = usuarioService.crear(request.nombre(), request.email(), request.password());
        return ResponseEntity.created(URI.create("/api/usuarios/" + usuario.id())).body(usuario);
    }

    public Usuario actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ActualizarUsuarioRequest request
    ) {
        return usuarioService.actualizar(id, request.nombre(), request.email());
    }

    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
