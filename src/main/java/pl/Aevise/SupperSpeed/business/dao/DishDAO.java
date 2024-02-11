package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;

import java.util.List;

public interface DishDAO {
    List<Dish> findAllByRestaurant(Integer restaurantId);

    List<Dish> findAllByCategory(Integer categoryId);

    void updateDish(DishDTO dishDTO);

    void deleteDish(Integer dishId);

    void deleteDishes(List<DishEntity> dishes);
}
