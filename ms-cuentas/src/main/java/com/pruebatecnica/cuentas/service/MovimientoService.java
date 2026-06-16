package com.pruebatecnica.cuentas.service;

import com.pruebatecnica.cuentas.dto.CrearMovimientoRequest;
import com.pruebatecnica.cuentas.dto.MovimientoResponse;
import com.pruebatecnica.cuentas.entity.Cuenta;
import com.pruebatecnica.cuentas.entity.Movimiento;
import com.pruebatecnica.cuentas.exception.BusinessException;
import com.pruebatecnica.cuentas.exception.ResourceNotFoundException;
import com.pruebatecnica.cuentas.exception.SaldoNoDisponibleException;
import com.pruebatecnica.cuentas.repository.CuentaRepository;
import com.pruebatecnica.cuentas.repository.MovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;

    @Transactional
    public MovimientoResponse registrarMovimiento(CrearMovimientoRequest request) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(request.numeroCuenta())
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));

        validarCuentaActiva(cuenta);
        validarValorMovimiento(request.valor());

        BigDecimal nuevoSaldo = cuenta.calcularNuevoSaldo(request.valor());

        validarSaldoDisponible(nuevoSaldo);

        cuenta.actualizarSaldo(nuevoSaldo);

        Movimiento movimiento = Movimiento.crear(
                cuenta,
                request.valor(),
                nuevoSaldo
        );

        Movimiento movimientoGuardado = movimientoRepository.save(movimiento);

        return MovimientoResponse.from(movimientoGuardado);
    }

    private void validarCuentaActiva(Cuenta cuenta) {
        if (!cuenta.estaActiva()) {
            throw new BusinessException("La cuenta se encuentra inactiva");
        }
    }

    private void validarValorMovimiento(BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) == 0) {
            throw new BusinessException("El valor del movimiento debe ser diferente de cero");
        }
    }

    private void validarSaldoDisponible(BigDecimal nuevoSaldo) {
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new SaldoNoDisponibleException("saldo no disponible");
        }
    }

    @Transactional(readOnly = true)
    public List<MovimientoResponse> listar() {
        return movimientoRepository.findAll()
                .stream()
                .map(MovimientoResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public MovimientoResponse buscarPorId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado"));

        return MovimientoResponse.from(movimiento);
    }
}