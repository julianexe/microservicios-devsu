package com.pruebatecnica.cuentas.controller;

import com.pruebatecnica.cuentas.dto.EstadoCuentaResponse;
import com.pruebatecnica.cuentas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes")
@RequiredArgsConstructor
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping
    public EstadoCuentaResponse generarEstadoCuenta(
            @RequestParam("fechaInicio")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaInicio,

            @RequestParam("fechaFin")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fechaFin,

            @RequestParam("clienteId")
            String clienteId
    ) {
        return reporteService.generarEstadoCuenta(fechaInicio, fechaFin, clienteId);
    }
}