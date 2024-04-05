package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishCategoryMapper;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishMapper;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DishListService {

    private final DishCategoryService dishCategoryService;
    private final DishCategoryMapper dishCategoryMapper;

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
    public HashMap<List<DishCategoryDTO>, List<DishDTO>> extractDishesByCategory(List<DishCategoryDTO> dishCategories) {
        HashMap<List<DishCategoryDTO>, List<DishDTO>> dishesByCategory = new HashMap<>();

        for (DishCategoryDTO dishCategory : dishCategories) {
            dishesByCategory.put(
                    List.of(dishCategory),
                    dishService
                            .findAllByCategory(dishCategory.getDishCategoryId())
                            .stream()
                            .map(dishMapper::mapToDTO)
                            .filter(DishDTO::getAvailability)
                            .toList()
            );
        }

        dishesByCategory.entrySet().removeIf(category -> category.getValue().isEmpty());
        return dishesByCategory;
    }

    public List<DishCategoryDTO> getDishCategoriesByRestaurantId(Integer restaurantId) {
        return dishCategoryService
                .findAllByRestaurant(restaurantId)
                .stream()
                .map(dishCategoryMapper::mapToDTO)
                .toList();
    }

}
