package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.IngredientEntity;

public interface IngredientJpaRepository extends JpaRepository<IngredientEntity, Integer> {
}
