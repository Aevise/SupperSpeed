package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;

import java.util.List;
import java.util.Optional;

public interface DishCategoryDAO {

    List<DishCategory> findAllByRestaurant(Integer restaurantId);

    void updateCategory(DishCategoryDTO dishCategory);

    void deleteCategoryAndRelatedDishes(Integer categoryId);

    void addCategory(DishCategoryEntity dishCategory);

    Optional<DishCategory> findById(Integer id);
}
