package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.domain.Image;

import java.util.List;
import java.util.Optional;

public interface DishDAO {
    List<Dish> findAllByCategory(Integer categoryId);

    Dish updateDish(DishDTO dishDTO);

    void deleteDish(Integer dishId);

    void deleteDishes(List<Dish> dishes);

    Dish addDish(Dish dish);

    void setDishImage(Image image, Integer dishId);

    Optional<Dish> permanentlyHideDishFromAllUsers(Integer dishId);

    void permanentlyHideDishesFromAllUsers(List<Dish> dishesInOrder);

    List<Dish> findAllNotHiddenDishes(String restaurantName);

    List<Dish> findNotHiddenDishesByCategory(String restaurantName, String category);

    Optional<Dish> findById(Integer dishId);
}
