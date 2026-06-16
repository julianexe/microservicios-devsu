package com.pruebatecnica.clientes.controller;

import com.pruebatecnica.clientes.dto.ActualizarClienteRequest;
import com.pruebatecnica.clientes.dto.ClienteResponse;
import com.pruebatecnica.clientes.dto.CrearClienteRequest;
import com.pruebatecnica.clientes.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crear(@Valid @RequestBody CrearClienteRequest request) {
        return clienteService.crear(request);
    }

    @GetMapping
    public List<ClienteResponse> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public ClienteResponse buscarPorId(@PathVariable Long id) {
        return clienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ActualizarClienteRequest request
    ) {
        return clienteService.actualizar(id, request);
    }

    @PatchMapping("/{id}")
    public ClienteResponse actualizarParcial(
            @PathVariable Long id,
            @RequestBody Map<String, Object> campos
    ) {
        return clienteService.actualizarParcial(id, campos);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
    }
}