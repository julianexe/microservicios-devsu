# Microservicios Financiero - Prueba Técnica DEVSU

Proyecto desarrollado para una prueba técnica para la gestión de clientes, cuentas bancarias, movimientos financieros y reportes.

hay 2 microservicios principales:

```text
ms-clientes
ms-cuentas
```

Se utiliza PostgreSQL como base de datos, RabbitMQ como broker de mensajería y Docker Compose para levantar toda la infraestructura del proyecto.

---

# 1. Arquitectura del proyecto

Son 2 microservicios independientes:

```text
ms-clientes
 ├── Gestion de Personas
 └── Gestion de Clientes

ms-cuentas
 ├── Gestion de Cuentas
 ├── Gestion de Movimientos
 ├── Gestion de ClienteProjection
 └── Generación de reportes de estado de cuentas
```

Cada servicio tiene su propia bd el cual respeta la independencia de datos.

```text
ms-clientes  -> clientes_db
ms-cuentas   -> cuentas_db
```

La infraestructura se levanta mediante Docker Compose:

```text
RabbitMQ
PostgreSQL clientes_db
PostgreSQL cuentas_db
ms-clientes
ms-cuentas
```

---

# 2. Tecnologías

```text
Java 21
Spring Boot
Spring Web
Spring Data JPA
Hibernate
PostgreSQL
Flyway
RabbitMQ
Docker
Docker Compose
JUnit 5
Maven
Postman
Lombok
```

---

## 3. Estructura interna del proyecto

Utilicé una arquitectura por capas:

```text
controller
dto
entity
exception
repository
service
```
---

# 4. Diseño de microservicios

## ms-clientes

Que hace?

```text
Gestiona clientes y datos de este.
```

Entidades:

```text
Persona
Cliente
```

se relacionan con extends y esto hace que cliente herede de persona

```text
Cliente extends Persona
```

Campos de `Persona`:

```text
id
nombre
genero
edad
identificacion
direccion
telefono
```

Campos de `Cliente`:

```text
clienteId
contrasena
estado
```

---

## ms-cuentas

Que hace?

```text
Gestiona cuentas, movimientos y reportes.
```

Entidades:

```text
ClienteProjection
Cuenta
Movimiento
```

`ClienteProjection` es una copia del cliente dentro de cuentas.

Eso evita que `ms-cuentas` dependa directamente de la base de datos de `ms-clientes`.

Campos de `ClienteProjection`:

```text
clienteId
nombre
identificacion
estado
```

Campos de `Cuenta`:

```text
id
numeroCuenta
tipoCuenta
saldoInicial
saldoDisponible
estado
clienteId
version
```

Campos de `Movimiento`:

```text
id
fecha
tipoMovimiento
valor
saldo
cuenta
```

---

# 5. Base de datos

Utilicé 2 bases de datos PostgreSQL independientes:

```text
clientes_db
cuentas_db
```

Esto separa las responsabilidades.

---

## Base de datos de clientes

Base:

```text
clientes_db
```

Tablas:

```text
personas
clientes
flyway_schema_history
```

La tabla `clientes` utiliza como PK `persona_id`, que también es FK hacia `personas`.

Esto se resume en herencia JPA `JOINED`.

---

## Base de datos de cuentas

Base:

```text
cuentas_db
```

Tablas:

```text
cliente_proyeccion
cuentas
movimientos
flyway_schema_history
```

La tabla `cliente_proyeccion` permite que `ms-cuentas` conozca los clientes activos sin consultar directamente la base de datos de `ms-clientes`.

---

## Flyway

El proyecto utiliza Flyway para versionar la base de datos.

Migraciones:

```text
ms-clientes/src/main/resources/db/migration/V1__create_clientes_schema.sql
ms-cuentas/src/main/resources/db/migration/V1__create_cuentas_schema.sql
```

---

## BaseDatos.sql

También se incluye un script consolidado ya que la prueba lo pide pero se gestiona es con Flyway para mas agilidad:

```text
database/BaseDatos.sql
```

---

# 6. RabbitMQ

RabbitMQ se levanta mediante Docker Compose:

```text
http://localhost:15672
```

Credenciales:

```text
usuario: guest
password: guest
```

RabbitMQ está incluido como infraestructura para soportar comunicación asincrónica entre microservicios con clientes publicando eventos y que cuentas los consuma.

```

Para alimentar la tabla de cliente_proyeccion use este sql:

```sql
INSERT INTO cliente_proyeccion (
    cliente_id,
    nombre,
    identificacion,
    estado
) VALUES
('CLI-001', 'Jose Lema', 'ID-001', true),
('CLI-002', 'Marianela Montalvo', 'ID-002', true),
('CLI-003', 'Juan Osorio', 'ID-003', true)
ON CONFLICT (cliente_id) DO UPDATE SET
    nombre = EXCLUDED.nombre,
    identificacion = EXCLUDED.identificacion,
    estado = EXCLUDED.estado;
```

---

# 7. Lógica de negocio

## Registro de movimientos

Un movimiento puede ser:

```text
DEPOSITO
RETIRO
```

regla utilizada:

```text
valor positivo -> depósito
valor negativo -> retiro
```

---

## Actualización de saldo

Cada movimiento actualiza el saldo disponible de la cuenta.

Fórmula:

```text
nuevoSaldo = saldoDisponible + valorMovimiento
```

---

## Saldo no disponible

No se permite que una cuenta quede con saldo negativo.

Si el nuevo saldo es menor que cero, se lanza una excepción de negocio con el mensaje:

```text
saldo no disponible
```

---

## Movimiento como hecho histórico y el no de la implementación del CRUD completo en esta parte

Los movimientos representan hechos financieros históricos.

Por esa razón, no deberían modificarse ni eliminarse.

---

## Control de concurrencia

La entidad `Cuenta` incluye un campo `version` con `@Version`.

Esto permite control de concurrencia optimista.

Es decir: esto evita si 2 movimientos intentan modificar la misma cuenta al mismo tiempo

---

---

# 9. Clean Code

La solución aplica varias prácticas de Clean Code y diseño mantenible.

---

## Separación por capas

Se separan responsabilidades en:

```text
controller -> expone endpoints REST
service -> contiene lógica de negocio
repository -> acceso a datos
entity -> modelo de dominio
dto -> entrada y salida de datos
exception -> manejo de errores
```

---

## DTOs para entrada y salida

No se exponen directamente las entidades JPA en las respuestas.

Se utilizan DTOs como:

```text
ClienteResponse
CuentaResponse
MovimientoResponse
EstadoCuentaResponse
```

Esto evita acoplar la API REST al modelo interno de persistencia.

---

## Validaciones de entrada

Se usan anotaciones de validación como:

```text
@NotBlank
@NotNull
@Min
@DecimalMin
```

Esto permite validar los requests antes de ejecutar la lógica de negocio.

---

## Manejo de excepciones

Se utiliza un `GlobalExceptionHandler` para retornar errores controlados.

Esto evita respuestas inconsistentes y mejora de consumo de la API.

---

## Transacciones

Las operaciones críticas se ejecutan con `@Transactional`.

Si una operación falla, se revierte todo.

---

## Uso de BigDecimal

Los saldos y valores monetarios se manejan con este tipo

---

---

# 10.  Java 26 y Java 21

El requerimiento inicial contemplaba Java 26. Inicialmente empecé con java 26 pero me dió muchos inconvenientes con el docker asi que ambié el proyecto a java 21 ya que se integra bastante bien con docker.

---

# 11. Ejecución del proyecto

---

## Levantar todo desde cero

Desde la raíz del proyecto:

```bash
docker compose down -v
docker compose up --build
```

---

## Levantar sin borrar datos

Si no se quieren perder los datos de PostgreSQL:

```bash
docker compose down
docker compose up --build
```

---

## Para ver contenedores activos

```bash
docker ps
```

lo que debería verse:

```text
rabbitmq
clientes-db
cuentas-db
clientes-service
cuentas-service
```

---

# 12. Validación de base de datos

## clientes_db

Entrar a la base:

```bash
docker exec -it clientes-db psql -U postgres -d clientes_db
```

Listar tablas:

```sql
\dt
```

Tablas esperadas:

```text
personas
clientes
flyway_schema_history
```

Salir:

```sql
\q
```

---

## cuentas_db

Entrar a la base:

```bash
docker exec -it cuentas-db psql -U postgres -d cuentas_db
```

Listar tablas:

```sql
\dt
```

Tablas esperadas:

```text
cliente_proyeccion
cuentas
movimientos
flyway_schema_history
```

Salir:

```sql
\q
```

---

# 13. Pruebas

## Prueba unitaria

Hay una prueba unitaria para `Cliente` (según lo pide la prueba).

Ruta:

```text
ms-clientes/src/test/java/com/pruebatecnica/clientes/entity/ClienteTest.java
```

La prueba valida comportamiento de dominio como:

```text
crear cliente activo
activar cliente
desactivar cliente
verificar estado activo
```

---

# 14. Colección Postman

La colección se encuentra en:

```text
postman/postman_collection.json
```

---

# 15. Endpoints

## Clientes

Base URL:

```text
http://localhost:8081/api
```

Endpoints:

```text
POST   /clientes
GET    /clientes
GET    /clientes/{id}
PUT    /clientes/{id}
PATCH  /clientes/{id}
DELETE /clientes/{id}
```

---

## Cuentas

Base URL:

```text
http://localhost:8082/api
```

Endpoints:

```text
POST  /cuentas
GET   /cuentas
GET   /cuentas/{id}
PUT   /cuentas/{id}
PATCH /cuentas/{id}
```

---

## Movimientos

Base URL:

```text
http://localhost:8082/api
```

Endpoints:

```text
POST /movimientos
GET  /movimientos
GET  /movimientos/{id}
```

---

## Reportes

Base URL:

```text
http://localhost:8082/api
```

Endpoint:

```text
GET /reportes?fechaInicio=2026-06-01&fechaFin=2026-06-30&clienteId=CLI-002
```