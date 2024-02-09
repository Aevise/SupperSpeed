package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DishCategoryDAO;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishCategoryJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DishCategoryEntityMapper;

import java.util.Collections;
import java.util.List;

@Repository
@AllArgsConstructor
public class DishCategoryRepository implements DishCategoryDAO {

    private final DishCategoryJpaRepository dishCategoryJpaRepository;
    private final DishCategoryEntityMapper dishCategoryEntityMapper;

    @Override
    public List<DishCategory> findAllByRestaurant(Integer restaurantId) {
        List<DishCategoryEntity> allByRestaurantId = dishCategoryJpaRepository.findAllByRestaurant_Id(restaurantId);

        List<DishCategory> list = allByRestaurantId.stream()
                .map(dishCategoryEntityMapper::mapFromEntity)
                .toList();


        return dishCategoryJpaRepository
                .findAllByRestaurant_Id(restaurantId)
                .stream()
                .map(dishCategoryEntityMapper::mapFromEntity)
                .toList();
    }
}
