package com.pruebatecnica.cuentas.dto;

import java.math.BigDecimal;
import java.util.List;

public record CuentaReporteResponse(
        String numeroCuenta,
        String tipoCuenta,
        BigDecimal saldoInicial,
        BigDecimal saldoDisponible,
        Boolean estado,
        List<MovimientoReporteResponse> movimientos
) {
}