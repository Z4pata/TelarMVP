-- =====================================================================
-- Query.sql | Base de datos: Gestión de Finanzas Personales
-- Motor: MySQL 8.0+ (InnoDB)
-- Orden: 1) Entidades fuertes  ->  2) Entidades dependientes
-- =====================================================================

CREATE DATABASE IF NOT EXISTS finanzas_personales
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE finanzas_personales;

-- =====================================================================
-- 1. ENTIDADES FUERTES (no dependen de ninguna otra tabla)
-- =====================================================================

-- Catálogo de tipos de cuenta (ej. Ahorros, Corriente, Efectivo)
CREATE TABLE IF NOT EXISTS tipo_cuenta (
    id          INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(50)  NOT NULL,
    descripcion VARCHAR(255) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_tipo_cuenta_nombre UNIQUE (nombre)
);

-- Usuarios de la aplicación
CREATE TABLE IF NOT EXISTS usuario (
    id            INT          NOT NULL AUTO_INCREMENT,
    nombre        VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,  -- Solo el hash (ej. BCrypt), nunca la contraseña en texto plano
    PRIMARY KEY (id),
    CONSTRAINT uq_usuario_email UNIQUE (email)  -- El email identifica al usuario en el login
);

-- Catálogo de tipos de movimiento (ej. Ingreso, Gasto)
CREATE TABLE IF NOT EXISTS tipo_movimiento (
    id          INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(50)  NOT NULL,
    descripcion VARCHAR(255) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_tipo_movimiento_nombre UNIQUE (nombre)
);

-- Catálogo de categorías (ej. Alimentación, Transporte, Salario)
CREATE TABLE IF NOT EXISTS categoria (
    id          INT          NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL,
    PRIMARY KEY (id),
    CONSTRAINT uq_categoria_nombre UNIQUE (nombre)
);

-- =====================================================================
-- 2. ENTIDADES DEPENDIENTES (contienen llaves foráneas)
-- =====================================================================

-- Cuenta financiera de un usuario (depende de: usuario, tipo_cuenta)
CREATE TABLE IF NOT EXISTS cuenta (
    id             INT          NOT NULL AUTO_INCREMENT,
    usuario_id     INT          NOT NULL,
    tipo_cuenta_id INT          NOT NULL,
    entidad        VARCHAR(100) NOT NULL,  -- Banco o entidad financiera
    descripcion    VARCHAR(255) NULL,
    PRIMARY KEY (id),
    -- Si se elimina el usuario, se eliminan sus cuentas
    CONSTRAINT fk_cuenta_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    -- No se puede borrar un tipo de cuenta que esté en uso
    CONSTRAINT fk_cuenta_tipo_cuenta
        FOREIGN KEY (tipo_cuenta_id) REFERENCES tipo_cuenta (id)
        ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Presupuesto de un usuario (depende de: usuario)
CREATE TABLE IF NOT EXISTS presupuesto (
    id           INT           NOT NULL AUTO_INCREMENT,
    usuario_id   INT           NOT NULL,
    monto_limite DECIMAL(15,2) NOT NULL,
    fecha_inicio DATETIME      NOT NULL,
    fecha_fin    DATETIME      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_presupuesto_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuario (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_presupuesto_monto CHECK (monto_limite > 0),
    -- El periodo debe ser válido
    CONSTRAINT chk_presupuesto_fechas CHECK (fecha_fin >= fecha_inicio)
);

-- Movimiento financiero (depende de: categoria, cuenta, tipo_movimiento)
CREATE TABLE IF NOT EXISTS movimiento_financiero (
    id                 INT           NOT NULL AUTO_INCREMENT,
    categoria_id       INT           NOT NULL,
    cuenta_id          INT           NOT NULL,
    tipo_movimiento_id INT           NOT NULL,
    descripcion        VARCHAR(255)  NULL,
    monto              DECIMAL(15,2) NOT NULL,  -- Siempre positivo; el signo lo define tipo_movimiento (ingreso/gasto)
    fecha              DATETIME      NOT NULL,
    PRIMARY KEY (id),
    -- Catálogos protegidos: no se pueden borrar si tienen movimientos asociados
    CONSTRAINT fk_movimiento_categoria
        FOREIGN KEY (categoria_id) REFERENCES categoria (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_movimiento_tipo_movimiento
        FOREIGN KEY (tipo_movimiento_id) REFERENCES tipo_movimiento (id)
        ON DELETE RESTRICT ON UPDATE CASCADE,
    -- Si se elimina la cuenta, se eliminan sus movimientos
    CONSTRAINT fk_movimiento_cuenta
        FOREIGN KEY (cuenta_id) REFERENCES cuenta (id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT chk_movimiento_monto CHECK (monto > 0)
);

-- Índice para acelerar consultas por rango de fechas (reportes y balances)
CREATE INDEX idx_movimiento_fecha ON movimiento_financiero (fecha);