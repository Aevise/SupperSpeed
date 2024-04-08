package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DishListDAO;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishesListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DishesListKey;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishesListJpaRepository;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DishListRepository implements DishListDAO {

    private final DishesListJpaRepository dishesListJpaRepository;

    @Override
    public Integer saveAllByOrderAndDishQuantity(List<DishesListEntity> dishesListEntities) {
        List<DishesListEntity> dishes = dishesListJpaRepository.saveAllAndFlush(dishesListEntities);
        return dishes.size();
    }

    @Override
    public void save(DishesListEntity dishesList) {
        dishesListJpaRepository.save(dishesList);
    }
}
