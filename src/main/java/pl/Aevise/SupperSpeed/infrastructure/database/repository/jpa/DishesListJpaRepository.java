package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishesListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DishesListKey;

import java.util.List;

public interface DishesListJpaRepository extends JpaRepository<DishesListEntity, DishesListKey> {

    List<DishesListEntity> findAllByOrder(SupperOrderEntity supperOrderEntity);

    List<DishesListEntity> findAllByDish_DishId(Integer dishId);
}
