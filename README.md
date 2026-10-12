# Belmonte SpA - Microservicio de Conductores (ms-conductores)

Gestiona de forma aislada a los conductores y su vehículo asignado. Es un servicio independiente
(no depende de otros microservicios); lo consumirá `ms-viajes` para validar y asignar conductores a un traslado.

## Tecnologías

- Java 25 / Spring Boot 4.0.6
- MySQL (Laragon) + Spring Data JPA, migraciones con Flyway
- Spring Cloud (cliente Eureka, desactivado por ahora)
- Datafaker para datos sintéticos (el anexo prohíbe datos reales)

## Puerto

`http://localhost:8083`

## Endpoints (`/api/v1/conductores`)

| Método | Ruta | Descripción |
|---|---|---|
| GET | `/` | Lista todos los conductores |
| GET | `/{id}` | Obtiene un conductor (404 si no existe) |
| POST | `/` | Crea un conductor (409 si email, licencia o patente ya existen) |
| PUT | `/{id}` | Actualiza datos (preserva `activo` y `disponible`) |
| DELETE | `/{id}` | Elimina un conductor |
| PATCH | `/{id}/estado` | Alterna el estado: activo (1) pasa a inactivo (0) y viceversa |
| PATCH | `/{id}/disponibilidad` | Alterna disponible (1) / ocupado (0) |
| GET | `/{id}/validar` | `true` si existe, está activo y disponible (lo usará ms-viajes vía Feign) |
| GET | `/activos` | Lista conductores activos |
| GET | `/disponibles` | Lista conductores activos y disponibles |

Ejemplo de `POST`:

```json
{
  "nombre": "Carlos Prueba",
  "email": "carlos@ejemplo.test",
  "telefono": "+56988887777",
  "numeroLicencia": "LIC-12345678",
  "patente": "BCDF12",
  "modeloVehiculo": "Toyota Hiace",
  "capacidadPasajeros": 12
}
```

## Base de datos (MySQL con Laragon)

1. Iniciar MySQL desde Laragon (puerto 3306).
2. La base `db_conductores` se crea sola al arrancar (`createDatabaseIfNotExist=true`).
3. Las tablas las crea Flyway al arrancar (`src/main/resources/db/migration`).
4. Credenciales por variables de entorno (por defecto, las de Laragon):

| Variable | Por defecto |
|---|---|
| `DB_URL` | `jdbc:mysql://localhost:3306/db_conductores?createDatabaseIfNotExist=true&serverTimezone=America/Santiago` |
| `DB_USER` | `root` |
| `DB_PASSWORD` | (vacía) |

## Ejecución local

El perfil `dev` viene activo en `application.yml`: si la tabla está vacía, carga 20 conductores sintéticos. La capacidad depende del vehículo: auto 4 pasajeros, SUV entre 4 y 6, van entre 6 y 10.

```bash
./mvnw spring-boot:run
```

## Nota

Si falta el wrapper de Maven (`mvnw`, carpeta `.mvn`), generarlo con `mvn wrapper:wrapper` o crearlo desde el IDE.
