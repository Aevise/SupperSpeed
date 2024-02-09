package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;

import java.util.List;

public interface DishCategoryJpaRepository extends JpaRepository<DishCategoryEntity, Integer> {

    List<DishCategoryEntity> findAllByRestaurant_Id(Integer restaurantId);
}
