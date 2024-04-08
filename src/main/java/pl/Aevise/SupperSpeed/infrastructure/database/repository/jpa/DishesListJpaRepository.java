package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishesListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DishesListKey;

import java.util.List;
import java.util.Optional;

public interface DishesListJpaRepository extends JpaRepository<DishesListEntity, DishesListKey> {

        List<DishesListEntity> findAllByOrder(SupperOrderEntity supperOrderEntity);
}
