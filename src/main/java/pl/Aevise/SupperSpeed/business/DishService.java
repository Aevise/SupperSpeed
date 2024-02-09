package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.Aevise.SupperSpeed.business.dao.DishDAO;
import pl.Aevise.SupperSpeed.domain.Dish;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class DishService {

    private final DishDAO dishDAO;

    public List<Dish> findAllByRestaurant(Integer restaurantId) {
        List<Dish> dishesFromRestaurant = dishDAO.findAllByRestaurant(restaurantId);
        log.info("Found: [{}] dishes", dishesFromRestaurant.size());
        return dishesFromRestaurant;
    }
}
