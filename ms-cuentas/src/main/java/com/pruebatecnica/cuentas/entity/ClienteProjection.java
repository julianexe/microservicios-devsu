package com.pruebatecnica.cuentas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "cliente_proyeccion")
@Getter
@Setter
public class ClienteProjection {

    @Id
    @Column(name = "cliente_id", nullable = false, length = 50)
    private String clienteId;

    @Column(name = "nombre", nullable = false, length = 120)
    private String nombre;

    @Column(name = "identificacion", nullable = false, length = 50)
    private String identificacion;

    @Column(name = "estado", nullable = false)
    private Boolean estado;

    public boolean estaActivo() {
        return Boolean.TRUE.equals(this.estado);
    }
}