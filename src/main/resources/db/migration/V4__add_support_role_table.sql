/**
 * Author:  Studios TKOH!
 * Created: Jan 28, 2026
 */

-- 1. Crear la tabla Usuario_Roles
CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL,
    rol VARCHAR(50) NOT NULL,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);
