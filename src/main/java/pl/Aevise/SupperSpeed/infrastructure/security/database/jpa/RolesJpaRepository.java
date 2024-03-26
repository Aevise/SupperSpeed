package pl.Aevise.SupperSpeed.infrastructure.security.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;

public interface RolesJpaRepository extends JpaRepository<RolesEntity, Integer> {
}
