package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;

public interface RestaurantResponseJpaRepository extends JpaRepository<RestaurantResponseEntity, Integer> {
}
