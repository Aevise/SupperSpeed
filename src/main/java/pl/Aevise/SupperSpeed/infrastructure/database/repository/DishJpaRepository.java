package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;

public interface DishJpaRepository extends JpaRepository<DishEntity, Integer> {
}
