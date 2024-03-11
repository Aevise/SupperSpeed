package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

import java.util.List;

public interface RestaurantJpaRepository extends JpaRepository<RestaurantEntity, Integer> {

    List<RestaurantEntity> findAllByAddress_City(String city);

}
