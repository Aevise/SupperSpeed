package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.Image;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;

public interface RestaurantDAO {
    Optional<Restaurant> findByName(String restaurantName);

    List<Restaurant> findAvailable();

    Optional<Restaurant> findByUserEmail(String email);

    void updateRestaurantInformation(Restaurant restaurant, Integer userId);

    Optional<Restaurant> findById(Integer id);

    List<Restaurant> findAllByCity(String city);

    RestaurantEntity createRestaurant(RestaurantEntity restaurantEntity);

    void updateRestaurantLogo(Integer userId, Image image);

    void toggleRestaurantVisibility(Integer userId);

    Restaurant detachUserFromRestaurant(String email);

    List<String> getDistinctCitiesWithRestaurants();

    Optional<AddressEntity> getAddressByRestaurantId(Integer restaurantId);

    List<Restaurant> findAllByCityAndCuisine(String city, String cuisine);
}
