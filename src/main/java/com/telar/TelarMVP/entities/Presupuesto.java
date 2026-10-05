package com.telar.TelarMVP.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Presupuesto {
    private Integer id;
    private Usuario usuario;
    private double montoLimite;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
}
