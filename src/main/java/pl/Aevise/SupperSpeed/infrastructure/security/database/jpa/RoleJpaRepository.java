package pl.Aevise.SupperSpeed.infrastructure.security.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;

public interface RoleJpaRepository extends JpaRepository<RolesEntity, Long> {
    RolesEntity findByRole(String role);
}
