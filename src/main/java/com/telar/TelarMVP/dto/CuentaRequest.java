package com.telar.TelarMVP.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaRequest {

    @NotNull(message = "El usuario es obligatorio")
    private Integer usuarioId;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private Integer tipoCuentaId;

    @NotBlank(message = "La entidad financiera es obligatoria")
    @Size(max = 100, message = "La entidad financiera no puede superar los 100 caracteres")
    private String entidad;

    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;
}
