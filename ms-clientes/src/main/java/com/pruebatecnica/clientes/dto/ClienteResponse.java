package com.pruebatecnica.clientes.dto;

import com.pruebatecnica.clientes.entity.Cliente;

public record ClienteResponse(
        Long id,
        String nombre,
        String genero,
        Integer edad,
        String identificacion,
        String direccion,
        String telefono,
        String clienteId,
        Boolean estado
) {

    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getNombre(),
                cliente.getGenero(),
                cliente.getEdad(),
                cliente.getIdentificacion(),
                cliente.getDireccion(),
                cliente.getTelefono(),
                cliente.getClienteId(),
                cliente.getEstado()
        );
    }
}