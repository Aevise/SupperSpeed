package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.domain.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantDAO {
    Optional<Restaurant> findByName(String restaurantName);

    List<Restaurant> findAvailable();

    void deleteRestaurantById(Integer id);

    Optional<Restaurant> findByEmail(String email);

    void updateRestaurantInformation(RestaurantDTO restaurantDTO, Integer userId);

    Optional<Restaurant> findById(Integer id);
}
