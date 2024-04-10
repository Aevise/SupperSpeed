package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DishListDAO;
import pl.Aevise.SupperSpeed.domain.DishList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishesListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishesListJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DishListEntityMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class DishListRepository implements DishListDAO {

    private final DishesListJpaRepository dishesListJpaRepository;
    private final DishListEntityMapper dishListEntityMapper;

    @Override
    public List<DishList> saveAllByOrderAndDishQuantity(List<DishesListEntity> dishesListEntities) {
        List<DishesListEntity> dishesEntity = dishesListJpaRepository.saveAllAndFlush(dishesListEntities);
        return dishesEntity.stream()
                .map(dishListEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public void save(DishesListEntity dishesList) {
        dishesListJpaRepository.save(dishesList);
    }
}
