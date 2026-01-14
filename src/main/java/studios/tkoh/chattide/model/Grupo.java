package studios.tkoh.chattide.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;

/**
 *
 * @author Studios TKOH!
 */
@Entity
@Table(name = "grupos")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class Grupo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @OneToMany(mappedBy = "grupo")
    private List<UsuarioGrupo> miembros = new ArrayList<>();
}
