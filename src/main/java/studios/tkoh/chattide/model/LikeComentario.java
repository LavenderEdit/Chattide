package studios.tkoh.chattide.model;

import jakarta.persistence.*;
import lombok.*;

/**
 *
 * @author Studios TKOH!
 */
@Entity
@Table(name = "likes_comentarios", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"usuario_id", "comentario_id"}) // Un usuario solo un like por comentario
})
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@AttributeOverride(name = "fechaRegistro", column = @Column(name = "fecha_like"))
public class LikeComentario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comentario_id", nullable = false)
    private Comentario comentario;
}
