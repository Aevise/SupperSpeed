package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;

import java.util.List;

public interface DishJpaRepository extends JpaRepository<DishEntity, Integer> {

    List<DishEntity> findAllByRestaurant_Id(Integer restaurantId);
    List<DishEntity> findAllByRestaurant_RestaurantNameAndRestaurant_IsShownAndIsHidden(String restaurantName, Boolean isShown, Boolean isHidden);
    List<DishEntity> findAllByRestaurant_RestaurantNameAndRestaurant_IsShownAndDishCategory_CategoryNameAndIsHidden(String restaurantName, Boolean isShown, String category, Boolean isHidden);
    List<DishEntity> findAllByDishCategory_DishCategoryId(Integer categoryId);
}
