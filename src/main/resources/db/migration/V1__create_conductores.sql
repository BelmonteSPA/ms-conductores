CREATE TABLE conductores (
    id_conductor        BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre              VARCHAR(150) NOT NULL,
    email               VARCHAR(150) NOT NULL UNIQUE,
    telefono            VARCHAR(20),
    numero_licencia     VARCHAR(20)  NOT NULL UNIQUE,
    patente             VARCHAR(10)  NOT NULL UNIQUE,
    modelo_vehiculo     VARCHAR(100) NOT NULL,
    capacidad_pasajeros INT          NOT NULL,
    disponible          BOOLEAN      NOT NULL DEFAULT TRUE,
    activo              BOOLEAN      NOT NULL DEFAULT TRUE
);
