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
        List<DishCategoryDTO> dishCategories = dishCategoryService
                .findAllByRestaurant(restaurantId)
                .stream()
                .map(dishCategoryMapper::mapToDTO)
                .toList();

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

}
