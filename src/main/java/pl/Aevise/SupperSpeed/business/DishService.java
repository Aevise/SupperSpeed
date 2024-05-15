package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishMapper;
import pl.Aevise.SupperSpeed.business.dao.DishDAO;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.domain.Image;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DishService {

    private final DishDAO dishDAO;
    private final RestaurantService restaurantService;
    private final DishCategoryService dishCategoryService;

    private final DishListService dishListService;
    private final DishMapper dishMapper;

    public List<Dish> findAllByRestaurant(Integer restaurantId) {
        List<Dish> dishesFromRestaurant = dishDAO.findAllByRestaurant(restaurantId);
        log.info("Found: [{}] dishesByRestaurant", dishesFromRestaurant.size());
        return dishesFromRestaurant;
    }

    public List<Dish> findAllByCategory(Integer categoryId) {
        List<Dish> dishesFromRestaurant = dishDAO.findAllByCategory(categoryId);
        log.info("Found: [{}] dishesByCategory", dishesFromRestaurant.size());
        return dishesFromRestaurant;
    }

    public void updateDish(DishDTO dishDTO) {
        dishDAO.updateDish(dishDTO);
        log.info("Updated dish: [{}] - [{}]", dishDTO.getDishId(), dishDTO.getName());
    }

    public void deleteOrHideDishByDishId(Integer dishId) {
        boolean dishUsed = dishListService.isDishInOrder(dishId);

        if (dishUsed) {
            Optional<Dish> dish = dishDAO.permanentlyHideDishFromAllUsers(dishId);
            if (dish.isPresent() && dish.get().getIsHidden()) {
                log.info("Dish [{}] hidden from the users", dishId);
            }
        } else {
            dishDAO.deleteDish(dishId);
            log.info("Deleted dish [{}]", dishId);
        }
    }

    @Transactional
    public void deleteOrHideDishesMap(Map<Boolean, List<Dish>> dishes) {
        List<Dish> dishesInOrder = dishes.get(true);
        if (!dishesInOrder.isEmpty()) {
            dishDAO.permanentlyHideDishesFromAllUsers(dishesInOrder);
            log.info("Hidden [{}] dishes", dishesInOrder.size());
        }

        List<Dish> dishesWithoutOrder = dishes.get(false);
        if (!dishesWithoutOrder.isEmpty()) {
            dishDAO.deleteDishes(dishesWithoutOrder);
            log.info("Deleted [{}] dishes", dishesWithoutOrder.size());
        }
    }

    @Transactional
    public void addDish(Dish dish) {

        dishDAO.addDish(dish);
        log.info("Successfully added dish: [{}]", dish.getName());
    }

    public Dish buildDish(DishDTO dishDTO, Integer restaurantId, Integer categoryId) {
        return Dish.builder()
                .name(dishDTO.getName())
                .price(dishDTO.getPrice())
                .description(dishDTO.getDescription())
                .availability(Optional
                        .ofNullable(dishDTO.getAvailability())
                        .orElse(false))
                .restaurant(restaurantService.findByIdEntity(restaurantId))
                .dishCategory(dishCategoryService.findByIdEntity(categoryId))
                .isHidden(false)
                .build();
    }

    public void setDishImage(Image image, Integer dishId) {
        dishDAO.setDishImage(image, dishId);
        log.info("Dish with id: [{}] image updated successfully", dishId);
    }

//    @Transactional
//    public HashMap<String, List<DishDTO>> getDishListByCategoryFromRestaurant(Integer restaurantId) {
//        List<DishCategoryDTO> dishCategories = getDishCategoriesByRestaurantId(restaurantId);
//        return extractDishesByCategoryName(dishCategories);
//    }

    @Transactional
    public HashMap<String, List<DishDTO>> extractDishesByCategoryName(List<DishCategoryDTO> dishCategories) {
        HashMap<String, List<DishDTO>> dishesByCategory = new HashMap<>();

        for (DishCategoryDTO dishCategory : dishCategories) {
            dishesByCategory.put(
                    dishCategory.getCategoryName(),
                    findAllByCategory(dishCategory.getDishCategoryId())
                            .stream()
                            .map(dishMapper::mapToDTO)
                            .toList()
            );
        }
        return dishesByCategory;
    }

    @Transactional
    public HashMap<List<DishCategoryDTO>, List<DishDTO>> extractDishesByCategory(List<DishCategoryDTO> dishCategories, boolean filterUnavailable) {
        HashMap<List<DishCategoryDTO>, List<DishDTO>> dishesByCategory = new HashMap<>();

        for (DishCategoryDTO dishCategory : dishCategories) {
            dishesByCategory.put(
                    List.of(dishCategory),
                    findAllByCategory(dishCategory.getDishCategoryId())
                            .stream()
                            .map(dishMapper::mapToDTO)
                            .filter(dishDTO -> !filterUnavailable || dishDTO.getAvailability())
                            .filter(dishDTO -> !dishDTO.getIsHidden())
                            .toList()
            );
        }
        if (filterUnavailable) {
            dishesByCategory.entrySet().removeIf(category -> category.getValue().isEmpty());
        }
        return dishesByCategory;
    }
}
