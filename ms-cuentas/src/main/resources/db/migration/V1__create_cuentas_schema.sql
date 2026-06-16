CREATE TABLE IF NOT EXISTS cliente_proyeccion (
    cliente_id VARCHAR(50) PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    identificacion VARCHAR(50) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE
    );

CREATE TABLE IF NOT EXISTS cuentas (
    id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(30) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(30) NOT NULL,
    saldo_inicial NUMERIC(19, 2) NOT NULL,
    saldo_disponible NUMERIC(19, 2) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    cliente_id VARCHAR(50) NOT NULL,
    version BIGINT DEFAULT 0,
    CONSTRAINT chk_cuentas_tipo_cuenta
    CHECK (tipo_cuenta IN ('AHORROS', 'CORRIENTE')),
    CONSTRAINT chk_cuentas_saldo_inicial_no_negativo
    CHECK (saldo_inicial >= 0),
    CONSTRAINT chk_cuentas_saldo_disponible_no_negativo
    CHECK (saldo_disponible >= 0),
    CONSTRAINT fk_cuentas_cliente_proyeccion
    FOREIGN KEY (cliente_id)
    REFERENCES cliente_proyeccion(cliente_id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
    );

CREATE TABLE IF NOT EXISTS movimientos (
   id BIGSERIAL PRIMARY KEY,
   cuenta_id BIGINT NOT NULL,
    fecha TIMESTAMP NOT NULL,
    tipo_movimiento VARCHAR(30) NOT NULL,
    valor NUMERIC(19, 2) NOT NULL,
    saldo NUMERIC(19, 2) NOT NULL,
    CONSTRAINT chk_movimientos_tipo_movimiento
    CHECK (tipo_movimiento IN ('DEPOSITO', 'RETIRO')),
    CONSTRAINT chk_movimientos_valor_diferente_cero
    CHECK (valor <> 0),
    CONSTRAINT chk_movimientos_saldo_no_negativo
    CHECK (saldo >= 0),
    CONSTRAINT fk_movimientos_cuentas
    FOREIGN KEY (cuenta_id)
    REFERENCES cuentas(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
    );

CREATE INDEX IF NOT EXISTS idx_cliente_proyeccion_identificacion
    ON cliente_proyeccion(identificacion);

CREATE INDEX IF NOT EXISTS idx_cuentas_cliente_id
    ON cuentas(cliente_id);

CREATE INDEX IF NOT EXISTS idx_cuentas_numero_cuenta
    ON cuentas(numero_cuenta);

CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta_id
    ON movimientos(cuenta_id);

CREATE INDEX IF NOT EXISTS idx_movimientos_fecha
    ON movimientos(fecha);

CREATE INDEX IF NOT EXISTS idx_movimientos_cuenta_fecha
    ON movimientos(cuenta_id, fecha);