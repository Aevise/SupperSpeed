package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.IngredientListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.IngredientListKey;

public interface IngredientListJpaRepository extends JpaRepository<IngredientListEntity, IngredientListKey> {
}
