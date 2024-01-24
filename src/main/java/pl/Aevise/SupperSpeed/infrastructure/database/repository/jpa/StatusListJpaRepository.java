package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.StatusListEntity;

public interface StatusListJpaRepository extends JpaRepository<StatusListEntity, Integer> {
}
