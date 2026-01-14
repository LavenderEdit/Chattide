package studios.tkoh.chattide.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Studios TKOH!
 */
@Entity
@Table(name = "likes")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Like extends BaseEntity {

    @CreationTimestamp
    @Column(name = "fecha_like", updatable = false)
    private LocalDateTime fechaLike;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "publicacion_id", nullable = false)
    private Publicacion publicacion;
}
