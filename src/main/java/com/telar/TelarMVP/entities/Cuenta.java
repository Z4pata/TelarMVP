package com.telar.TelarMVP.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cuenta")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    @NotNull(message = "El usuario es obligatorio")
    private Integer usuarioId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "tipo_cuenta_id", nullable = false)
    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipoCuenta;

    @Column(name = "entidad", nullable = false, length = 100)
    @NotBlank(message = "La entidad financiera es obligatoria")
    @Size(max = 100, message = "La entidad financiera no puede superar los 100 caracteres")
    private String entidad;

    @Column(name = "descripcion", length = 255)
    @Size(max = 255, message = "La descripcion no puede superar los 255 caracteres")
    private String descripcion;
}
