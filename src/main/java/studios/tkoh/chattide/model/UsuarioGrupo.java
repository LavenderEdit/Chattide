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
@Table(name = "usuario_grupo")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class UsuarioGrupo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grupo_id", nullable = false)
    private Grupo grupo;

    @CreationTimestamp
    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;

    private String rol; // ADMIN, MIEMBRO
}
