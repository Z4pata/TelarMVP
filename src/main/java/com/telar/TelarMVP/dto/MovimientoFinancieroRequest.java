package com.telar.TelarMVP.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class MovimientoFinancieroRequest {

    @NotNull(message = "La categoria es obligatoria")
    private Integer categoriaId;

    @NotNull(message = "La cuenta es obligatoria")
    private Integer cuentaId;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private Integer tipoMovimientoId;

    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor que cero")
    private BigDecimal monto;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime fecha;
}
