package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.business.dao.DishDAO;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DishService {

    private final DishDAO dishDAO;

    public List<Dish> findAllByRestaurant(Integer restaurantId) {
        List<Dish> dishesFromRestaurant = dishDAO.findAllByRestaurant(restaurantId);
        log.info("Found: [{}] dishesByCategory", dishesFromRestaurant.size());
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

    public void deleteDish(Integer dishId) {
        dishDAO.deleteDish(dishId);
        log.info("Deleted dish [{}]", dishId);
    }

    public void deleteDishes(List<DishEntity> dishes) {
        dishDAO.deleteDishes(dishes);
        log.info("Deleted [{}] dishes", dishes.size());
    }

    @Transactional
    public void addDish(Dish dish) {
        dishDAO.addDish(dish);
        log.info("Successfully added dish: [{}]", dish.getName());
    }
}
