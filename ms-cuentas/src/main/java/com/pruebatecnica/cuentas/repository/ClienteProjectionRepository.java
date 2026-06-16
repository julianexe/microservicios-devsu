package com.pruebatecnica.cuentas.repository;

import com.pruebatecnica.cuentas.entity.ClienteProjection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteProjectionRepository extends JpaRepository<ClienteProjection, String> {
}