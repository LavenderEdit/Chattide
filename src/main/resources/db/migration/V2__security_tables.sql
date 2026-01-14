/**
 * Author:  Studios TKOH!
 * Created: Jan 13, 2026
 */

-- 1. Tabla para Tokens de Recuperación (OTP)
CREATE TABLE password_reset_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(10) NOT NULL,
    fecha_expiracion DATETIME NOT NULL,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_otp_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

-- 2. Tabla para Refresh Tokens (Gestión de Sesión JWT)
CREATE TABLE refresh_tokens (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    fecha_expiracion DATETIME NOT NULL,
    revocado BOOLEAN DEFAULT FALSE,
    usuario_id BIGINT NOT NULL,
    CONSTRAINT fk_refresh_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token ON refresh_tokens(token);
CREATE INDEX idx_otp_token ON password_reset_tokens(token);