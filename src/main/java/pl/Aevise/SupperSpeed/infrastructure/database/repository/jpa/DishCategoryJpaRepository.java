package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;

import java.util.List;
import java.util.Optional;

public interface DishCategoryJpaRepository extends JpaRepository<DishCategoryEntity, Integer> {

    List<DishCategoryEntity> findAllByRestaurant_Id(Integer restaurantId);

    Optional<DishCategoryEntity> findByCategoryName(String categoryName);
}
