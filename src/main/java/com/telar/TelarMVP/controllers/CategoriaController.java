package com.telar.TelarMVP.controllers;

import com.telar.TelarMVP.dto.CategoriaRequest;
import com.telar.TelarMVP.entities.Categoria;
import com.telar.TelarMVP.interfaces.Api.CategoriaApiInterface;
import com.telar.TelarMVP.interfaces.service.CategoriaServiceInterface;
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

@Tag(name = "Categoria", description = "Endpoints para el manejo de categorías de movimientos financieros")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/categorias")
public class CategoriaController implements CategoriaApiInterface {

    private final CategoriaServiceInterface categoriaService;

    public CategoriaController(CategoriaServiceInterface categoriaService) {
        this.categoriaService = categoriaService;
    }

    @Override
    public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaRequest request) {
        Categoria categoria = categoriaService.insertar(request);
        return ResponseEntity.created(URI.create("/api/categorias/" + categoria.getId()))
                .body(categoria);
    }

    @Override
    public ResponseEntity<List<Categoria>> listar() {
        return ResponseEntity.ok(categoriaService.listar());
    }

    @Override
    public ResponseEntity<Categoria> findByNombre(@PathVariable String nombre) {
        return ResponseEntity.ok(categoriaService.findByNombre(nombre));
    }

    @Override
    public ResponseEntity<Categoria> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(categoriaService.findById(id));
    }

    @Override
    public ResponseEntity<Categoria> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody CategoriaRequest request
    ) {
        return ResponseEntity.ok(categoriaService.actualizar(id, request));
    }

    @Override
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        categoriaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
