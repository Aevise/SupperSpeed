package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishesListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DishesListKey;

public interface DishesListJpaRepository extends JpaRepository<DishesListEntity, DishesListKey> {
}
