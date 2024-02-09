package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.Cuisine;

import java.util.List;

public interface CuisineDAO {
    List<Cuisine> findAllByRestaurant(Integer restaurantId);
}
