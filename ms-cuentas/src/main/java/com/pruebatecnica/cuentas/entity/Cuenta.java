package com.pruebatecnica.cuentas.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cuentas")
@Getter
@Setter
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_cuenta", nullable = false, unique = true, length = 30)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_cuenta", nullable = false, length = 30)
    private TipoCuenta tipoCuenta;

    @Column(name = "saldo_inicial", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoInicial;

    @Column(name = "saldo_disponible", nullable = false, precision = 19, scale = 2)
    private BigDecimal saldoDisponible;

    @Column(name = "estado", nullable = false)
    private Boolean estado = Boolean.TRUE;

    @Column(name = "cliente_id", nullable = false, length = 50)
    private String clienteId;

    @Version
    @Column(name = "version")
    private Long version;

    @OneToMany(
            mappedBy = "cuenta",
            cascade = CascadeType.ALL,
            orphanRemoval = false,
            fetch = FetchType.LAZY
    )
    private List<Movimiento> movimientos = new ArrayList<>();

    public boolean estaActiva() {
        return Boolean.TRUE.equals(this.estado);
    }

    public boolean tieneSaldoSuficiente(BigDecimal valor) {
        BigDecimal nuevoSaldo = this.saldoDisponible.add(valor);
        return nuevoSaldo.compareTo(BigDecimal.ZERO) >= 0;
    }

    public BigDecimal calcularNuevoSaldo(BigDecimal valor) {
        return this.saldoDisponible.add(valor);
    }

    public void actualizarSaldo(BigDecimal nuevoSaldo) {
        this.saldoDisponible = nuevoSaldo;
    }

    public void desactivar() {
        this.estado = Boolean.FALSE;
    }
}