package pl.Aevise.SupperSpeed.business;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.Aevise.SupperSpeed.business.dao.RestaurantDAO;
import pl.Aevise.SupperSpeed.domain.Restaurant;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class RestaurantViewService {

    private final RestaurantDAO restaurantDAO;

    @Transactional
    public List<Restaurant> availableRestaurants() {
        List<Restaurant> availableRestaurants = restaurantDAO.findAvailable();
        log.info("Number of available restaurants: [{}]", availableRestaurants.size());
        return availableRestaurants;
    }

}
