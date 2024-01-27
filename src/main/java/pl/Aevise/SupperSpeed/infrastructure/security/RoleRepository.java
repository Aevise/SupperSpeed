package pl.Aevise.SupperSpeed.infrastructure.security;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RolesEntity, Long> {
    RolesEntity findByRole(String role);
}
