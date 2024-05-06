package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishCategoryMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishListMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishMapper;
import pl.Aevise.SupperSpeed.domain.DishList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishesListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DishesListKey;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.DishListRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class DishListService {

    private final DishCategoryService dishCategoryService;
    private final DishCategoryMapper dishCategoryMapper;

    private final DishListRepository dishListRepository;
    private final DishListMapper dishListMapper;

    private final DishService dishService;
    private final DishMapper dishMapper;


    @Transactional
    public HashMap<String, List<DishDTO>> getDishListByCategoryFromRestaurant(Integer restaurantId) {
        List<DishCategoryDTO> dishCategories = getDishCategoriesByRestaurantId(restaurantId);
        return extractDishesByCategoryName(dishCategories);
    }

    @Transactional
    public HashMap<String, List<DishDTO>> extractDishesByCategoryName(List<DishCategoryDTO> dishCategories) {
        HashMap<String, List<DishDTO>> dishesByCategory = new HashMap<>();

        for (DishCategoryDTO dishCategory : dishCategories) {
            dishesByCategory.put(
                    dishCategory.getCategoryName(),
                    dishService
                            .findAllByCategory(dishCategory.getDishCategoryId())
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
                    dishService
                            .findAllByCategory(dishCategory.getDishCategoryId())
                            .stream()
                            .map(dishMapper::mapToDTO)
                            .filter(dishDTO -> !filterUnavailable || dishDTO.getAvailability())
                            .toList()
            );
        }
        if (filterUnavailable) {
            dishesByCategory.entrySet().removeIf(category -> category.getValue().isEmpty());
        }
        return dishesByCategory;
    }

    @Transactional
    public List<DishListDTO> getDishesByOrderId(int orderId) {
        List<DishList> dishesByOrderId = dishListRepository.getDishesByOrderId(orderId);
        if (!dishesByOrderId.isEmpty()) {
            log.info("Found [{}] dishes bound with order [{}]", dishesByOrderId.size(), orderId);

            return dishesByOrderId.stream()
                    .map(dishListMapper::mapToDTO)
                    .toList();
        }
        log.warn("Could not find dishes for order [{}]", orderId);
        return List.of();

    }


    public List<DishCategoryDTO> getDishCategoriesByRestaurantId(Integer restaurantId) {
        return dishCategoryService
                .findAllByRestaurant(restaurantId)
                .stream()
                .map(dishCategoryMapper::mapToDTO)
                .toList();
    }


    @Transactional
    public List<DishListDTO> saveAllByOrderAndDishQuantity(Integer orderId, Map<Integer, Integer> dishQuantities) {
        List<DishesListEntity> dishesListEntities = bindDishesWithOrder(orderId, dishQuantities);
        List<DishList> savedDishes = dishListRepository.saveAllByOrderAndDishQuantity(dishesListEntities);

        List<DishListDTO> dishesDTO = savedDishes.stream()
                .map(dishListMapper::mapToDTO)
                .toList();

        if (!dishesDTO.isEmpty()) {
            log.info("Successfully saved [{}] dishes for order: [{}]", savedDishes, orderId);
            return dishesDTO;
        } else {
            log.warn("Did not save any dishes for order: [{}]", orderId);
            return List.of();
        }
    }

    private List<DishesListEntity> bindDishesWithOrder(Integer orderId, Map<Integer, Integer> dishQuantities) {
        List<DishesListEntity> dishes = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : dishQuantities.entrySet()) {
            dishes.add(DishesListEntity.builder()
                    .id(DishesListKey.builder()
                            .orderId(orderId)
                            .dishId(entry.getKey())
                            .build())
                    .dish(DishEntity.builder()
                            .dishId(entry.getKey())
                            .build())
                    .order(SupperOrderEntity.builder()
                            .orderId(orderId)
                            .build())
                    .quantity(entry.getValue())
                    .build());
        }
        log.info("Bound [{}] dishes with order: [{}]", dishes.size(), orderId);
        return dishes;
    }
}
