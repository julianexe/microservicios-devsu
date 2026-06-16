package com.pruebatecnica.cuentas.dto;

import com.pruebatecnica.cuentas.entity.TipoCuenta;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CrearCuentaRequest(

        @NotBlank(message = "El número de cuenta es obligatorio")
        String numeroCuenta,

        @NotNull(message = "El tipo de cuenta es obligatorio")
        TipoCuenta tipoCuenta,

        @NotNull(message = "El saldo inicial es obligatorio")
        @DecimalMin(value = "0.00", message = "El saldo inicial no puede ser negativo")
        BigDecimal saldoInicial,

        Boolean estado,

        @NotBlank(message = "El clienteId es obligatorio")
        String clienteId
) {
}