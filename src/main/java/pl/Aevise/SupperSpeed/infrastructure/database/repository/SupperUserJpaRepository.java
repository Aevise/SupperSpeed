package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperUserEntity;

public interface SupperUserJpaRepository extends JpaRepository<SupperUserEntity, SupperUserEntity> {
}
