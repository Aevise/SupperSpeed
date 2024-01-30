package pl.Aevise.SupperSpeed.infrastructure.security.database.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;

import java.util.Optional;

public interface SupperUserJpaRepository extends JpaRepository<SupperUserEntity, Integer> {

    Optional<SupperUserEntity> findByEmail(String email);
}
