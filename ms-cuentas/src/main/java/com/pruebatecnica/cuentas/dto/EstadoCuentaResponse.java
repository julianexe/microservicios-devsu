package com.pruebatecnica.cuentas.dto;

import java.time.LocalDate;
import java.util.List;

public record EstadoCuentaResponse(
        ClienteReporteResponse cliente,
        LocalDate fechaInicio,
        LocalDate fechaFin,
        List<CuentaReporteResponse> cuentas
) {
}