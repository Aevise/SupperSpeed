package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;

public interface DishJpaRepository extends JpaRepository<DishEntity, Integer> {
}
