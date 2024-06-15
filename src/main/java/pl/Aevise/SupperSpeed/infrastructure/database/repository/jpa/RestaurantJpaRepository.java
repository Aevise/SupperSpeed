package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Integer> {

    List<RestaurantEntity> findAllByAddress_City(String city);

    Optional<RestaurantEntity> findBySupperUser_Email(String email);

    @Query("SELECT DISTINCT a.city FROM RestaurantEntity r JOIN r.address a WHERE a.city IS NOT NULL AND a.streetName IS NOT NULL")
    List<String> findDistinctCitiesForRestaurants();

    @Query("SELECT r.address FROM RestaurantEntity r WHERE r.id = :restaurantId")
    Optional<AddressEntity> findAddressByRestaurantId(Integer restaurantId);

}
