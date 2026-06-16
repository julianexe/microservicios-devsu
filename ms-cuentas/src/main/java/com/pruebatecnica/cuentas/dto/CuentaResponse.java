package com.pruebatecnica.cuentas.dto;

import com.pruebatecnica.cuentas.entity.Cuenta;

import java.math.BigDecimal;

public record CuentaResponse(
        Long id,
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Boolean estado,
        String clienteId
) {

    public static CuentaResponse from(Cuenta cuenta) {
        return new CuentaResponse(
                cuenta.getId(),
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta().name(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.getEstado(),
                cuenta.getClienteId()
        );
    }
}