package com.pruebatecnica.cuentas.service;

import com.pruebatecnica.cuentas.dto.ActualizarCuentaRequest;
import com.pruebatecnica.cuentas.dto.CrearCuentaRequest;
import com.pruebatecnica.cuentas.dto.CuentaResponse;
import com.pruebatecnica.cuentas.entity.ClienteProjection;
import com.pruebatecnica.cuentas.entity.Cuenta;
import com.pruebatecnica.cuentas.exception.BusinessException;
import com.pruebatecnica.cuentas.exception.ResourceNotFoundException;
import com.pruebatecnica.cuentas.repository.ClienteProjectionRepository;
import com.pruebatecnica.cuentas.repository.CuentaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CuentaService {

    private final CuentaRepository cuentaRepository;
    private final ClienteProjectionRepository clienteProjectionRepository;

    @Transactional
    public CuentaResponse crear(CrearCuentaRequest request) {
        validarNumeroCuentaNoDuplicado(request.numeroCuenta());

        ClienteProjection cliente = clienteProjectionRepository.findById(request.clienteId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        if (!cliente.estaActivo()) {
            throw new BusinessException("El cliente se encuentra inactivo");
        }

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(request.numeroCuenta());
        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setSaldoInicial(request.saldoInicial());
        cuenta.setSaldoDisponible(request.saldoInicial());
        cuenta.setEstado(request.estado() != null ? request.estado() : Boolean.TRUE);
        cuenta.setClienteId(request.clienteId());

        Cuenta cuentaGuardada = cuentaRepository.save(cuenta);

        return CuentaResponse.from(cuentaGuardada);
    }

    @Transactional(readOnly = true)
    public List<CuentaResponse> listar() {
        return cuentaRepository.findAll()
                .stream()
                .map(CuentaResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public CuentaResponse buscarPorId(Long id) {
        Cuenta cuenta = obtenerCuenta(id);
        return CuentaResponse.from(cuenta);
    }

    @Transactional
    public CuentaResponse actualizar(Long id, ActualizarCuentaRequest request) {
        Cuenta cuenta = obtenerCuenta(id);

        cuenta.setTipoCuenta(request.tipoCuenta());
        cuenta.setEstado(request.estado());

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        return CuentaResponse.from(cuentaActualizada);
    }

    @Transactional
    public CuentaResponse actualizarParcial(Long id, Map<String, Object> campos) {
        Cuenta cuenta = obtenerCuenta(id);

        if (campos.containsKey("estado")) {
            cuenta.setEstado((Boolean) campos.get("estado"));
        }

        Cuenta cuentaActualizada = cuentaRepository.save(cuenta);

        return CuentaResponse.from(cuentaActualizada);
    }

    private Cuenta obtenerCuenta(Long id) {
        return cuentaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuenta no encontrada"));
    }

    private void validarNumeroCuentaNoDuplicado(String numeroCuenta) {
        if (cuentaRepository.existsByNumeroCuenta(numeroCuenta)) {
            throw new BusinessException("Ya existe una cuenta con el número indicado");
        }
    }
}