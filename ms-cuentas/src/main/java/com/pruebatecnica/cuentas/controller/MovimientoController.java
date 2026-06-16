package com.pruebatecnica.cuentas.controller;

import com.pruebatecnica.cuentas.dto.CrearMovimientoRequest;
import com.pruebatecnica.cuentas.dto.MovimientoResponse;
import com.pruebatecnica.cuentas.service.MovimientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MovimientoResponse crearMovimiento(
            @Valid @RequestBody CrearMovimientoRequest request
    ) {
        return movimientoService.registrarMovimiento(request);
    }

    @GetMapping
    public List<MovimientoResponse> listar() {
        return movimientoService.listar();
    }

    @GetMapping("/{id}")
    public MovimientoResponse buscarPorId(@PathVariable Long id) {
        return movimientoService.buscarPorId(id);
    }
}