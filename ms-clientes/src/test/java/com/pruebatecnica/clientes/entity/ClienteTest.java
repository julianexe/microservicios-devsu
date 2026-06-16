package com.pruebatecnica.clientes.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    @Test
    @DisplayName("Debe crear un cliente activo correctamente")
    void debeCrearClienteActivoCorrectamente() {
        Cliente cliente = new Cliente();

        cliente.setNombre("Jose Lema");
        cliente.setGenero("Masculino");
        cliente.setEdad(35);
        cliente.setIdentificacion("ID-001");
        cliente.setDireccion("Otavalo sn");
        cliente.setTelefono("310123");
        cliente.setClienteId("CLI-001");
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        assertEquals("Jose Lema", cliente.getNombre());
        assertEquals("Masculino", cliente.getGenero());
        assertEquals(35, cliente.getEdad());
        assertEquals("ID-001", cliente.getIdentificacion());
        assertEquals("Otavalo sn", cliente.getDireccion());
        assertEquals("310123", cliente.getTelefono());
        assertEquals("CLI-001", cliente.getClienteId());
        assertEquals("1234", cliente.getContrasena());
        assertTrue(cliente.getEstado());
        assertTrue(cliente.estaActivo());
    }

    @Test
    @DisplayName("Debe desactivar un cliente correctamente")
    void debeDesactivarClienteCorrectamente() {
        Cliente cliente = new Cliente();
        cliente.setEstado(true);

        cliente.desactivar();

        assertFalse(cliente.getEstado());
        assertFalse(cliente.estaActivo());
    }

    @Test
    @DisplayName("Debe activar un cliente correctamente")
    void debeActivarClienteCorrectamente() {
        Cliente cliente = new Cliente();
        cliente.setEstado(false);

        cliente.activar();

        assertTrue(cliente.getEstado());
        assertTrue(cliente.estaActivo());
    }
}