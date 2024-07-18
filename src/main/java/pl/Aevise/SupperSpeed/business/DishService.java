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
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

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

    public List<DishDTO> findAllNotHiddenDishes(String restaurantName) {
        List<Dish> dishesFromRestaurant = dishDAO.findAllNotHiddenDishes(restaurantName);
        if (dishesFromRestaurant.isEmpty()) {
            log.info("No dishes for restaurant [{}] found", restaurantName);
            return List.of();
        }
        List<DishDTO> dishList = dishesFromRestaurant.stream()
                .map(dishMapper::mapToDTO)
                .toList();
        log.info("[{}] dishes found for restaurant with id: [{}]", dishList.size(), restaurantName);
        return dishList;
    }

    public List<DishDTO> findNotHiddenDishesByCategory(String restaurantName, String category) {
        List<Dish> dishesFromRestaurant = dishDAO.findNotHiddenDishesByCategory(restaurantName, category);
        if (dishesFromRestaurant.isEmpty()) {
            log.info("No dishes for restaurant [{}] found", restaurantName);
            return List.of();
        }
        List<DishDTO> dishList = dishesFromRestaurant.stream()
                .map(dishMapper::mapToDTO)
                .toList();
        log.info("[{}] dishes found for restaurant with id: [{}] and category [{}]",
                dishList.size(), restaurantName, category);
        return dishList;
    }

    public List<Dish> findAllByCategory(Integer categoryId) {
        List<Dish> dishesFromRestaurant = dishDAO.findAllByCategory(categoryId);
        log.info("Found: [{}] dishesByCategory", dishesFromRestaurant.size());
        return dishesFromRestaurant;
    }

    public DishDTO updateDish(DishDTO dishDTO) {
        Dish dish = dishDAO.updateDish(dishDTO);
        log.info("Updated dish: [{}] - [{}]", dishDTO.getDishId(), dishDTO.getName());
        return dishMapper.mapToDTO(dish);
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
    public DishDTO addDish(DishDTO dishDTO) {
        Dish dish = dishDAO.addDish(dishMapper.mapFromDTO(dishDTO));
        log.info("Successfully added dish: [{}]", dish.getName());
        return dishMapper.mapToDTO(dish);
    }

    @Transactional
    public Dish addDish(Dish dish) {
        Dish newDish = dishDAO.addDish(dish);
        log.info("Successfully added dish: [{}]", newDish.getName());
        return newDish;
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

    public Dish buildDish(DishDTO dishDTO, RestaurantEntity restaurantEntity, DishCategoryEntity dishCategoryEntity) {
        return Dish.builder()
                .name(dishDTO.getName())
                .price(dishDTO.getPrice())
                .description(dishDTO.getDescription())
                .availability(Optional
                        .ofNullable(dishDTO.getAvailability())
                        .orElse(false))
                .restaurant(restaurantEntity)
                .dishCategory(dishCategoryEntity)
                .isHidden(false)
                .build();
    }


    public Dish buildDish(DishDTO dishDTO, Integer restaurantId, String categoryName) {
        return Dish.builder()
                .name(dishDTO.getName())
                .price(dishDTO.getPrice())
                .description(dishDTO.getDescription())
                .availability(Optional
                        .ofNullable(dishDTO.getAvailability())
                        .orElse(false))
                .restaurant(restaurantService.findByIdEntity(restaurantId))
                .dishCategory(dishCategoryService.findByName(categoryName))
                .isHidden(false)
                .build();
    }

    public void setDishImage(Image image, Integer dishId) {
        dishDAO.setDishImage(image, dishId);
        log.info("Dish with id: [{}] image updated successfully", dishId);
    }

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
                            .filter(dish -> !filterUnavailable || dish.getAvailability())
                            .filter(dish -> !dish.getIsHidden())
                            .map(dishMapper::mapToDTO)
                            .toList()
            );
        }
        if (filterUnavailable) {
            dishesByCategory.entrySet().removeIf(category -> category.getValue().isEmpty());
        }
        return dishesByCategory;
    }

    public DishDTO findById(Integer dishId) {
        Optional<Dish> dish = dishDAO.findById(dishId);

        if (dish.isPresent()) {
            log.info("Found dish with id: [{}]", dishId);
            return dishMapper.mapToDTO(dish.get());
        }
        log.info("Dish with id: [{}] not found", dishId);
        return null;
    }

    public Dish findByIdPOJO(Integer dishId) {
        Optional<Dish> dish = dishDAO.findById(dishId);

        if (dish.isPresent()) {
            log.info("Found dish with id: [{}]", dishId);
            return dish.get();
        }
        log.info("Dish with id: [{}] not found", dishId);
        return null;
    }
}
