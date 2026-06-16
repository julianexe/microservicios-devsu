package com.pruebatecnica.clientes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearClienteRequest(

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        String genero,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 0, message = "La edad no puede ser negativa")
        Integer edad,

        @NotBlank(message = "La identificación es obligatoria")
        String identificacion,

        @NotBlank(message = "La dirección es obligatoria")
        String direccion,

        @NotBlank(message = "El teléfono es obligatorio")
        String telefono,

        @NotBlank(message = "El clienteId es obligatorio")
        String clienteId,

        @NotBlank(message = "La contraseña es obligatoria")
        String contrasena,

        Boolean estado
) {
}