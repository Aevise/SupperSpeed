package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.DishCategory;

import java.util.List;

public interface DishCategoryDAO {

    List<DishCategory> findAllByRestaurant(Integer restaurantId);
}
