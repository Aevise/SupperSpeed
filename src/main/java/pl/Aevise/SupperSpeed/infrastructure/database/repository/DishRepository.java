package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DishDAO;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DishEntityMapper;

import java.util.Collections;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishRepository implements DishDAO {
    private final DishJpaRepository dishJpaRepository;
    private final DishEntityMapper dishEntityMapper;

    @Override
    public List<Dish> findAllByRestaurant(Integer restaurantId) {
        return dishJpaRepository
                .findAllById(Collections.singleton(restaurantId))
                .stream()
                .map(dishEntityMapper::mapFromEntity)
                .toList();
    }
}
