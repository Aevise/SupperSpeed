package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

public interface SupperOrderJpaRepository extends JpaRepository<SupperOrderEntity, Integer> {
}
