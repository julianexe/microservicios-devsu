package com.pruebatecnica.cuentas.dto;

import com.pruebatecnica.cuentas.entity.Movimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record MovimientoReporteResponse(
        LocalDateTime fecha,
        String tipoMovimiento,
        BigDecimal valor,
        BigDecimal saldoDisponible
) {

    public static MovimientoReporteResponse from(Movimiento movimiento) {
        return new MovimientoReporteResponse(
                movimiento.getFecha(),
                movimiento.getTipoMovimiento().name(),
                movimiento.getValor(),
                movimiento.getSaldo()
        );
    }
}