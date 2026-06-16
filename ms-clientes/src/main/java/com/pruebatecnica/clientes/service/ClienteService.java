package com.pruebatecnica.clientes.service;

import com.pruebatecnica.clientes.dto.ActualizarClienteRequest;
import com.pruebatecnica.clientes.dto.ClienteResponse;
import com.pruebatecnica.clientes.dto.CrearClienteRequest;
import com.pruebatecnica.clientes.entity.Cliente;
import com.pruebatecnica.clientes.exception.BusinessException;
import com.pruebatecnica.clientes.exception.ResourceNotFoundException;
import com.pruebatecnica.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponse crear(CrearClienteRequest request) {
        validarClienteNoDuplicado(request);

        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setGenero(request.genero());
        cliente.setEdad(request.edad());
        cliente.setIdentificacion(request.identificacion());
        cliente.setDireccion(request.direccion());
        cliente.setTelefono(request.telefono());
        cliente.setClienteId(request.clienteId());
        cliente.setContrasena(request.contrasena());
        cliente.setEstado(request.estado() != null ? request.estado() : Boolean.TRUE);

        Cliente clienteGuardado = clienteRepository.save(cliente);

        return ClienteResponse.from(clienteGuardado);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = obtenerCliente(id);
        return ClienteResponse.from(cliente);
    }

    @Transactional
    public ClienteResponse actualizar(Long id, ActualizarClienteRequest request) {
        Cliente cliente = obtenerCliente(id);

        cliente.setNombre(request.nombre());
        cliente.setGenero(request.genero());
        cliente.setEdad(request.edad());
        cliente.setDireccion(request.direccion());
        cliente.setTelefono(request.telefono());
        cliente.setContrasena(request.contrasena());
        cliente.setEstado(request.estado());

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return ClienteResponse.from(clienteActualizado);
    }

    @Transactional
    public ClienteResponse actualizarParcial(Long id, Map<String, Object> campos) {
        Cliente cliente = obtenerCliente(id);

        if (campos.containsKey("nombre")) {
            cliente.setNombre((String) campos.get("nombre"));
        }

        if (campos.containsKey("genero")) {
            cliente.setGenero((String) campos.get("genero"));
        }

        if (campos.containsKey("edad")) {
            cliente.setEdad((Integer) campos.get("edad"));
        }

        if (campos.containsKey("direccion")) {
            cliente.setDireccion((String) campos.get("direccion"));
        }

        if (campos.containsKey("telefono")) {
            cliente.setTelefono((String) campos.get("telefono"));
        }

        if (campos.containsKey("contrasena")) {
            cliente.setContrasena((String) campos.get("contrasena"));
        }

        if (campos.containsKey("estado")) {
            cliente.setEstado((Boolean) campos.get("estado"));
        }

        Cliente clienteActualizado = clienteRepository.save(cliente);

        return ClienteResponse.from(clienteActualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        Cliente cliente = obtenerCliente(id);

        cliente.desactivar();

        clienteRepository.save(cliente);
    }

    private Cliente obtenerCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
    }

    private void validarClienteNoDuplicado(CrearClienteRequest request) {
        if (clienteRepository.existsByClienteId(request.clienteId())) {
            throw new BusinessException("Ya existe un cliente con el clienteId indicado");
        }

        if (clienteRepository.existsByIdentificacion(request.identificacion())) {
            throw new BusinessException("Ya existe un cliente con la identificación indicada");
        }
    }
}