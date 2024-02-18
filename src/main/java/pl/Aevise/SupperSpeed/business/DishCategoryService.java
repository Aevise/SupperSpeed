package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.business.dao.DishCategoryDAO;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.RestaurantEntityMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DishCategoryService {

    private final DishCategoryDAO dishCategoryDAO;
    private final RestaurantService restaurantService;
    private final RestaurantEntityMapper restaurantEntityMapper;

    private static DishCategoryEntity buildDishCategory(String categoryName, RestaurantEntity restaurantEntity) {
        return DishCategoryEntity
                .builder()
                .restaurant(restaurantEntity)
                .categoryName(categoryName)
                .build();
    }

    @Transactional
    public List<DishCategory> findAllByRestaurant(Integer restaurantId) {
        List<DishCategory> dishCategories = dishCategoryDAO.findAllByRestaurant(restaurantId);
        log.info("Found: [{}] categories", dishCategories.size());
        return dishCategories;
    }

    public void updateCategory(DishCategoryDTO dishCategory) {
        dishCategoryDAO.updateCategory(dishCategory);
        log.info("Updated category: [{}]", dishCategory.getDishCategoryId());
    }

    public void deleteCategory(Integer categoryId) {
        dishCategoryDAO.deleteCategoryAndRelatedDishes(categoryId);
        log.info("Deleted category: [{}]", categoryId);
    }

    public void addCategory(Integer restaurantId, String categoryName) {
        Optional<Restaurant> restaurant = restaurantService.findById(restaurantId);
        if (restaurant.isPresent()) {
            RestaurantEntity restaurantEntity = getRestaurant(restaurantId, restaurant);
            DishCategoryEntity newCategory = buildDishCategory(categoryName, restaurantEntity);
            dishCategoryDAO.addCategory(newCategory);
            log.info("Added category: [{}] - [{}]", restaurantId, categoryName);
        }
        log.error("Could't add category: [{}]", categoryName);
    }

    private RestaurantEntity getRestaurant(Integer restaurantId, Optional<Restaurant> restaurant) {
        RestaurantEntity restaurantEntity = restaurantEntityMapper.mapToEntity(restaurant.get());
        restaurantEntity.setId(restaurantId);
        return restaurantEntity;
    }
}
