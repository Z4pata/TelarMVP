package com.telar.TelarMVP.interfaces.Api;

import com.telar.TelarMVP.dto.CategoriaRequest;
import com.telar.TelarMVP.entities.Categoria;
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

public interface CategoriaApiInterface {

    @Operation(summary = "Crear una categoría")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Categoría creada"),
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
    ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaRequest request);

    @Operation(summary = "Listar todas las categorías")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    @GetMapping
    ResponseEntity<List<Categoria>> listar();

    @Operation(summary = "Encontrar una categoría por su nombre")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la categoría",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/nombre/{nombre}")
    ResponseEntity<Categoria> findByNombre(@PathVariable("nombre") String nombre);

    @Operation(summary = "Encontrar una categoría por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la categoría",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @GetMapping("/{id}")
    ResponseEntity<Categoria> findById(@PathVariable("id") Integer id);

    @Operation(summary = "Actualizar una categoría por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Categoría actualizada"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos no válidos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la categoría",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "La operación viola una restricción de la base de datos",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @PutMapping("/{id}")
    ResponseEntity<Categoria> actualizar(
            @PathVariable("id") Integer id,
            @Valid @RequestBody CategoriaRequest request
    );

    @Operation(summary = "Eliminar una categoría por su ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Categoría eliminada"),
            @ApiResponse(
                    responseCode = "404",
                    description = "No existe la categoría",
                    content = @Content(schema = @Schema(implementation = ApiExceptionHandler.ApiError.class))
            )
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> eliminar(@PathVariable("id") Integer id);
}
