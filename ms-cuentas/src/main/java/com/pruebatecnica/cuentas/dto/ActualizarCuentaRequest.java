package com.pruebatecnica.cuentas.dto;

import com.pruebatecnica.cuentas.entity.TipoCuenta;
import jakarta.validation.constraints.NotNull;

public record ActualizarCuentaRequest(

        @NotNull(message = "El tipo de cuenta es obligatorio")
        TipoCuenta tipoCuenta,

        @NotNull(message = "El estado es obligatorio")
        Boolean estado
) {
}