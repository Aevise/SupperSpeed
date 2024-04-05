package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DishListDAO;

@Repository
@AllArgsConstructor
public class DishListRepository implements DishListDAO {
    @Override
    public void bindDishesWithOrder() {

    }
}
