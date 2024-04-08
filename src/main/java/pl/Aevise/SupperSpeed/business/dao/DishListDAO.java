package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishesListEntity;

import java.util.List;

public interface DishListDAO {

    Integer saveAllByOrderAndDishQuantity(List<DishesListEntity> dishesListEntities);

    void save(DishesListEntity dishesList);
}
