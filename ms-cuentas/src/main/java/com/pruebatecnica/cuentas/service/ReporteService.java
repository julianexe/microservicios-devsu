package com.pruebatecnica.cuentas.service;

import com.pruebatecnica.cuentas.dto.ClienteReporteResponse;
import com.pruebatecnica.cuentas.dto.CuentaReporteResponse;
import com.pruebatecnica.cuentas.dto.EstadoCuentaResponse;
import com.pruebatecnica.cuentas.dto.MovimientoReporteResponse;
import com.pruebatecnica.cuentas.entity.ClienteProjection;
import com.pruebatecnica.cuentas.entity.Cuenta;
import com.pruebatecnica.cuentas.entity.Movimiento;
import com.pruebatecnica.cuentas.exception.BusinessException;
import com.pruebatecnica.cuentas.exception.ResourceNotFoundException;
import com.pruebatecnica.cuentas.repository.ClienteProjectionRepository;
import com.pruebatecnica.cuentas.repository.CuentaRepository;
import com.pruebatecnica.cuentas.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteService {

    private final ClienteProjectionRepository clienteProjectionRepository;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Transactional(readOnly = true)
    public EstadoCuentaResponse generarEstadoCuenta(
            LocalDate fechaInicio,
            LocalDate fechaFin,
            String clienteId
    ) {
        validarRangoFechas(fechaInicio, fechaFin);

        ClienteProjection cliente = clienteProjectionRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        List<Cuenta> cuentas = cuentaRepository.findByClienteId(clienteId);

        LocalDateTime inicio = fechaInicio.atStartOfDay();
        LocalDateTime fin = fechaFin.plusDays(1).atStartOfDay().minusNanos(1);

        List<CuentaReporteResponse> cuentasResponse = cuentas.stream()
                .map(cuenta -> construirCuentaReporte(cuenta, inicio, fin))
                .toList();

        ClienteReporteResponse clienteResponse = new ClienteReporteResponse(
                cliente.getClienteId(),
                cliente.getNombre(),
                cliente.getIdentificacion(),
                cliente.getEstado()
        );

        return new EstadoCuentaResponse(
                clienteResponse,
                fechaInicio,
                fechaFin,
                cuentasResponse
        );
    }

    private CuentaReporteResponse construirCuentaReporte(
            Cuenta cuenta,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    ) {
        List<Movimiento> movimientos = movimientoRepository
                .findByCuentaIdAndFechaBetweenOrderByFechaAsc(
                        cuenta.getId(),
                        fechaInicio,
                        fechaFin
                );

        List<MovimientoReporteResponse> movimientosResponse = movimientos.stream()
                .map(MovimientoReporteResponse::from)
                .toList();

        return new CuentaReporteResponse(
                cuenta.getNumeroCuenta(),
                cuenta.getTipoCuenta().name(),
                cuenta.getSaldoInicial(),
                cuenta.getSaldoDisponible(),
                cuenta.getEstado(),
                movimientosResponse
        );
    }

    private void validarRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new BusinessException("Las fechas son obligatorias");
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new BusinessException("La fecha inicial no puede ser mayor que la fecha final");
        }
    }
}