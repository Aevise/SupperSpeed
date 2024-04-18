package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.RestaurantResponse;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;

public interface RestaurantResponseDAO {
    RestaurantResponseEntity saveRestaurantResponse(RestaurantResponse restaurantResponse);
}
