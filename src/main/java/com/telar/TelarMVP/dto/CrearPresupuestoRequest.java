package com.telar.TelarMVP.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CrearPresupuestoRequest {

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El monto debe ser mayor a 0")
    private double montoLimite;

    @Future(message = "La fecha de fin debe ser en el futuro")
    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    @NotNull(message = "El id del usuario es obligatorio")
    private Integer usuarioId;
}
