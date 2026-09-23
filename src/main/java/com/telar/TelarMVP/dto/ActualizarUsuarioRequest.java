package com.telar.TelarMVP.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ActualizarUsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        @Size(max = 150, message = "El email no puede superar 150 caracteres")
        String email
) {
}
