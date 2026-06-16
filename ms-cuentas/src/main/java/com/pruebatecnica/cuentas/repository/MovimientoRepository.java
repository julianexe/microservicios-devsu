package com.pruebatecnica.cuentas.repository;

import com.pruebatecnica.cuentas.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaAsc(
            Long cuentaId,
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin
    );
}