package com.pruebatecnica.cuentas.dto;

public record ClienteReporteResponse(
        String clienteId,
        String nombre,
        String identificacion,
        Boolean estado
) {
}