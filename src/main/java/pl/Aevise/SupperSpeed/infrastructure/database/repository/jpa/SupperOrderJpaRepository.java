package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

import java.util.List;

public interface SupperOrderJpaRepository extends JpaRepository<SupperOrderEntity, Integer> {
    List<SupperOrderEntity> findAllByRestaurant_Id(Integer restaurantId);

    List<SupperOrderEntity> findAllByClient_Id(Integer clientId);
}
