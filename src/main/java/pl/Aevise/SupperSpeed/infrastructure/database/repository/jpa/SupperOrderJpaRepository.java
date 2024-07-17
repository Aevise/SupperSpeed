package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

import java.util.List;

public interface SupperOrderJpaRepository extends JpaRepository<SupperOrderEntity, Integer> {
    List<SupperOrderEntity> findAllByRestaurant_Id(Integer restaurantId);

    List<SupperOrderEntity> findAllByClient_Id(Integer clientId);

    Page<SupperOrderEntity> findAllByRestaurant_IdAndUserRatingIsNotNull(Integer restaurantId, PageRequest pageRequest);

    Page<SupperOrderEntity> findAllByRestaurant_RestaurantNameAndUserRatingIsNotNull(String restaurantName, PageRequest pageRequest);

    List<SupperOrderEntity> findAllByRestaurant_IdAndUserRatingIsNotNull(Integer restaurantId);

    List<SupperOrderEntity> findAllByRestaurant_RestaurantNameAndUserRatingIsNotNull(String restaurantName);


}
