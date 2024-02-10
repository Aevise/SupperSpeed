package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishMapper;
import pl.Aevise.SupperSpeed.business.dao.DishDAO;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DishEntityMapper;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DishRepository implements DishDAO {
    private final DishJpaRepository dishJpaRepository;
    private final DishEntityMapper dishEntityMapper;
    private final DishMapper dishMapper;

    @Override
    public List<Dish> findAllByRestaurant(Integer restaurantId) {
        return dishJpaRepository
                .findAllByRestaurant_Id(restaurantId)
                .stream()
                .map(dishEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public List<Dish> findAllByCategory(Integer categoryId) {
        return dishJpaRepository
                .findAllByDishCategory_DishCategoryId(categoryId)
                .stream()
                .map(dishEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public void updateDish(DishDTO dishDTO) {
        DishEntity dish = dishJpaRepository
                .findById(dishDTO.getDishId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dish with id: [%s] not found"
                                .formatted(dishDTO.getDishId())
                ));
        Dish newDishData = dishMapper.mapFromDTO(dishDTO);

        dish.setName(newDishData.getName());
        dish.setDescription(newDishData.getDescription());
        dish.setPrice(newDishData.getPrice());
        dish.setAvailability(newDishData.getAvailability());

        dishJpaRepository.saveAndFlush(dish);
    }

}
