package studios.tkoh.chattide.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import studios.tkoh.chattide.model.Grupo;
import studios.tkoh.chattide.model.Usuario;
import studios.tkoh.chattide.model.UsuarioGrupo;

/**
 *
 * @author Studios TKOH!
 */
@Repository
public interface UsuarioGrupoRepository extends JpaRepository<UsuarioGrupo, Long> {

    Optional<UsuarioGrupo> findByUsuarioAndGrupo(Usuario usuario, Grupo grupo);

    Optional<UsuarioGrupo> findByUsuarioIdAndGrupoId(Long usuarioId, Long grupoId);

    boolean existsByUsuarioIdAndGrupoId(Long usuarioId, Long grupoId);

    List<UsuarioGrupo> findByUsuarioId(Long usuarioId);

    List<UsuarioGrupo> findByGrupoId(Long grupoId);

    long countByGrupoId(Long grupoId);
}
