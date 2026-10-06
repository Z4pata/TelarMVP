package com.telar.TelarMVP.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(UsuarioNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(UsuarioNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(Instant.now(), 404, exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ApiError> manejarNoEncontrado(EmptyResultDataAccessException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(
                        Instant.now(),
                        404,
                        "No se encontro el registro.",
                        Map.of()
                ));
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ApiError> manejarContraseniaIncorrecta(IncorrectPasswordException ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ApiError(
                        Instant.now(),
                        401,
                        "Contrasenia incorrecta.",
                        Map.of()
                ));
    }

    @ExceptionHandler(ConflictoExcepcion.class)
    public ResponseEntity<ApiError> manejarConflicto(ConflictoExcepcion exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(Instant.now(), 409, exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ApiError> manejarConflicto(
            IncorrectResultSizeDataAccessException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(
                        Instant.now(),
                        409,
                        "Se encontraron múltiples registros cuando solo debía existir uno.",
                        Map.of()
                ));
    }

    @ExceptionHandler(PresupuestoNoEncontradoException.class)
    public ResponseEntity<ApiError> manejarPresupuestoNoEncontrado(PresupuestoNoEncontradoException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiError(Instant.now(), 404, exception.getMessage(), Map.of()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarValidacion(MethodArgumentNotValidException exception) {
        Map<String, String> errores = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> errores.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return ResponseEntity.badRequest()
                .body(new ApiError(Instant.now(), 400, "Datos de entrada invalidos", errores));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> manejarConflicto(DataIntegrityViolationException exception) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiError(
                        Instant.now(),
                        409,
                        "El email ya esta registrado o la operacion viola una restriccion de la base de datos",
                        Map.of()
                ));
    }

    public record ApiError(
            Instant timestamp,
            int status,
            String mensaje,
            Map<String, String> errores
    ) {
    }
}
