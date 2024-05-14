package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.business.dao.DishCategoryDAO;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DishCategoryEntityMapper;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class DishCategoryService {

    private final DishCategoryDAO dishCategoryDAO;
    private final RestaurantService restaurantService;
    private final DishCategoryEntityMapper dishCategoryEntityMapper;

    public DishCategory buildDishCategory(String restaurantId, String categoryName) {
        return DishCategory.builder()
                .categoryName(categoryName)
                .restaurant(restaurantService.findByIdEntity(Integer.valueOf(restaurantId)))
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

        dishCategoryDAO.deleteCategory(categoryId);
        log.info("Deleted category: [{}]", categoryId);
    }

    public void addCategory(DishCategory dishCategory) {
        DishCategoryEntity dishCategoryEntity = dishCategoryEntityMapper.mapToEntity(dishCategory);
        dishCategoryDAO.addCategory(dishCategoryEntity);
        log.info("successfully added dishCategory: [{}] to restaurant [{}]",
                dishCategory.getCategoryName(),
                dishCategory.getRestaurant().getId());
    }

    public Optional<DishCategory> findById(Integer id) {
        Optional<DishCategory> dishCategory = dishCategoryDAO.findById(id);
        if (dishCategory.isPresent()) {
            log.info("Successfully found dish category with id: [{}]", id);
            return dishCategory;
        }
        log.warn("Could not find dish category with id: [{}]", id);
        return Optional.empty();
    }

    public DishCategoryEntity findByIdEntity(Integer id) {
        Optional<DishCategory> dishCategory = findById(id);
        if (dishCategory.isPresent()) {
            DishCategoryEntity dishCategoryEntity = dishCategoryEntityMapper.mapToEntity(dishCategory.get());
            log.info("Successfully mapped dish category to entity");
            return dishCategoryEntity;
        }
        log.warn("No dish category to map");
        return null;
    }

}
