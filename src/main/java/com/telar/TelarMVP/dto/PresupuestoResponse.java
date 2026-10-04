package com.telar.TelarMVP.dto;

import com.telar.TelarMVP.entities.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
public class PresupuestoResponse {
    private Integer id;
    private Integer usuarioId;
    private double montoLimite;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
