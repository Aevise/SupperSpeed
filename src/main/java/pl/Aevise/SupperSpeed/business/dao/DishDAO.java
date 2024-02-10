package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.domain.Dish;

import java.util.List;

public interface DishDAO {
    List<Dish> findAllByRestaurant(Integer restaurantId);

    List<Dish> findAllByCategory(Integer categoryId);

    void updateDish(DishDTO dishDTO);
}
