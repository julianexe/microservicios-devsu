CREATE TABLE IF NOT EXISTS personas (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    genero VARCHAR(30),
    edad INTEGER,
    identificacion VARCHAR(50) NOT NULL UNIQUE,
    direccion VARCHAR(180),
    telefono VARCHAR(50),
    CONSTRAINT chk_personas_edad_no_negativa
    CHECK (edad IS NULL OR edad >= 0)
    );

CREATE TABLE IF NOT EXISTS clientes (
    persona_id BIGINT PRIMARY KEY,
    cliente_id VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    CONSTRAINT fk_clientes_personas
    FOREIGN KEY (persona_id)
    REFERENCES personas(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
    );

CREATE INDEX IF NOT EXISTS idx_clientes_cliente_id
    ON clientes(cliente_id);

CREATE INDEX IF NOT EXISTS idx_personas_identificacion
    ON personas(identificacion);

CREATE INDEX IF NOT EXISTS idx_personas_nombre
    ON personas(nombre);