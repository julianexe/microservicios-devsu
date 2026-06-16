package com.pruebatecnica.cuentas.controller;

import com.pruebatecnica.cuentas.dto.ActualizarCuentaRequest;
import com.pruebatecnica.cuentas.dto.CrearCuentaRequest;
import com.pruebatecnica.cuentas.dto.CuentaResponse;
import com.pruebatecnica.cuentas.service.CuentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cuentas")
@RequiredArgsConstructor
public class CuentaController {

    private final CuentaService cuentaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CuentaResponse crear(@Valid @RequestBody CrearCuentaRequest request) {
        return cuentaService.crear(request);
    }

    @GetMapping
    public List<CuentaResponse> listar() {
        return cuentaService.listar();
    }

    @GetMapping("/{id}")
    public CuentaResponse buscarPorId(@PathVariable Long id) {
        return cuentaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public CuentaResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarCuentaRequest request
    ) {
        return cuentaService.actualizar(id, request);
    }

    @PatchMapping("/{id}")
    public CuentaResponse actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> campos
    ) {
        return cuentaService.actualizarParcial(id, campos);
    }
}