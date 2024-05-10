package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.Image;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;

public interface RestaurantDAO {
    Optional<Restaurant> findByName(String restaurantName);

    List<Restaurant> findAvailable();

    void deleteRestaurantById(Integer id);

    Optional<Restaurant> findByEmail(String email);

    void updateRestaurantInformation(Restaurant restaurant, Integer userId);

    Optional<Restaurant> findById(Integer id);

    List<Restaurant> findAll();

    List<Restaurant> findAllByCity(String city);

    RestaurantEntity createRestaurant(RestaurantEntity restaurantEntity);

    void updateRestaurantLogo(Integer userId, Image image);

    void toggleRestaurantVisibility(Integer userId);
}
