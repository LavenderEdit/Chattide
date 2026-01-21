/**
 * Author:  Studios TKOH!
 * Created: Jan 21, 2026
 */
/* MIGRACIÓN V3: Soporte para Likes en Comentarios */

CREATE TABLE likes_comentarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_like DATETIME DEFAULT CURRENT_TIMESTAMP,
    usuario_id BIGINT NOT NULL,
    comentario_id BIGINT NOT NULL,
    CONSTRAINT fk_lc_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    CONSTRAINT fk_lc_comentario FOREIGN KEY (comentario_id) REFERENCES comentarios(id) ON DELETE CASCADE,
    UNIQUE KEY uk_like_user_comment (usuario_id, comentario_id)
);