--Cimientos, utf-8 para caracteres especiales
CREATE DATABASE IF NOT EXISTS dasntscam
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

--Tabla usuarios
CREATE TABLE IF NOT EXISTS usuarios (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(50),
    apellido_1 VARCHAR(50),
    apellido_2 VARCHAR(50),
    sexo ENUM('M', 'F'),

    numero_dni VARCHAR(20) UNIQUE,
    correo_electronico VARCHAR(100) UNIQUE,
    numero_contacto VARCHAR(20),
    fecha_nacimiento DATE,

    pais VARCHAR(50),
    localidad VARCHAR(100),
    municipio VARCHAR(100),
    codigo_postal VARCHAR(10),

    rol ENUM('cliente', 'perito', 'admin') NOT NULL DEFAULT 'cliente',
    contrasena_hash CHAR(60) NOT NULL,

    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--Tabla peritos
CREATE TABLE IF NOT EXISTS peritos (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNSIGNED NOT NULL,

    colegio_profesional VARCHAR(100) NOT NULL,
    numero_colegiado VARCHAR(30) NOT NULL,
    codigo_registro VARCHAR(30) NOT NULL,

    cv LONGBLOB,

    CONSTRAINT fk_peritos_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id)
        ON DELETE CASCADE

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--Tabla clientes
CREATE TABLE IF NOT EXISTS clientes (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNSIGNED NOT NULL,

    libro_verde VARCHAR(100) NOT NULL,
    poliza_seguro VARCHAR(30) NOT NULL,

    CONSTRAINT fk_clientes_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id)
        ON DELETE CASCADE

)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--Tabla admin
CREATE TABLE IF NOT EXISTS admins (
    id INT UNSIGNED AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT UNSIGNED NOT NULL,

    nombre VARCHAR(100) NOT NULL,

    CONSTRAINT fk_admin_usuario
        FOREIGN KEY (id_usuario)
        REFERENCES usuarios(id)
        ON DELETE CASCADE

)
ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;