package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.domain.Image;

import java.util.List;
import java.util.Optional;

public interface DishDAO {
    List<Dish> findAllByRestaurant(Integer restaurantId);

    List<Dish> findAllByCategory(Integer categoryId);

    void updateDish(DishDTO dishDTO);

    void deleteDish(Integer dishId);

    void deleteDishes(List<Dish> dishes);

    void addDish(Dish dish);

    void setDishImage(Image image, Integer dishId);

    Optional<Dish> permanentlyHideDishFromAllUsers(Integer dishId);

    void permanentlyHideDishesFromAllUsers(List<Dish> dishesInOrder);
}
