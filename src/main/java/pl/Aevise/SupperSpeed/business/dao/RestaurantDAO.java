package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantDAO {
    Optional<Restaurant> findByName(String restaurantName);

    List<Restaurant> findAvailable();
}
