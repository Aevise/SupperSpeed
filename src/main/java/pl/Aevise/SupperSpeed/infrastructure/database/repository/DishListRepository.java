package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DishListDAO;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishesListJpaRepository;

@Repository
@AllArgsConstructor
public class DishListRepository implements DishListDAO {

    private final DishesListJpaRepository dishesListJpaRepository;

    @Override
    public void bindDishesWithOrder() {
        dishesListJpaRepository.saveAllAndFlush();
    }
}
