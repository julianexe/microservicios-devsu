package com.pruebatecnica.clientes.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "persona_id")
@Getter
@Setter
public class Cliente extends Persona {

    @Column(name = "cliente_id", nullable = false, unique = true, length = 50)
    private String clienteId;

    @Column(name = "contrasena", nullable = false, length = 255)
    private String contrasena;

    @Column(name = "estado", nullable = false)
    private Boolean estado = Boolean.TRUE;

    public boolean estaActivo() {
        return Boolean.TRUE.equals(this.estado);
    }

    public void activar() {
        this.estado = Boolean.TRUE;
    }

    public void desactivar() {
        this.estado = Boolean.FALSE;
    }
}