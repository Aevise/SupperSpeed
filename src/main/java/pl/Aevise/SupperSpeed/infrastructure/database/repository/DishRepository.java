package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishMapper;
import pl.Aevise.SupperSpeed.business.dao.DishDAO;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.domain.Image;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ImageEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DishEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.ImageEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DishRepository implements DishDAO {
    private final DishJpaRepository dishJpaRepository;
    private final DishEntityMapper dishEntityMapper;
    private final DishMapper dishMapper;

    private final ImageEntityMapper imageEntityMapper;


    @Override
    public List<Dish> findAllByCategory(Integer categoryId) {
        return dishJpaRepository
                .findAllByDishCategory_DishCategoryId(categoryId)
                .stream()
                .map(dishEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Dish updateDish(DishDTO dishDTO) {
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
        if(newDishData.getAvailability() == null){
            dish.setAvailability(false);
        }else {
            dish.setAvailability(newDishData.getAvailability());
        }

        DishEntity dishEntity = dishJpaRepository.saveAndFlush(dish);
        return dishEntityMapper.mapFromEntity(dishEntity);
    }

    @Override
    public void deleteDish(Integer dishId) {
        dishJpaRepository.deleteById(dishId);
    }

    @Override
    public void deleteDishes(List<Dish> dishes) {
        List<DishEntity> dishEntityList = dishes.stream().map(dishEntityMapper::mapToEntity).toList();
        dishJpaRepository.deleteAll(dishEntityList);
    }

    @Override
    public Dish addDish(Dish dish) {
        DishEntity dishToSave = dishEntityMapper.mapToEntity(dish);
        DishEntity dishEntity = dishJpaRepository.saveAndFlush(dishToSave);
        return dishEntityMapper.mapFromEntity(dishEntity);
    }

    @Override
    public void setDishImage(Image image, Integer dishId) {
        Optional<DishEntity> dishEntity = dishJpaRepository.findById(dishId);
        if (dishEntity.isPresent()) {
            ImageEntity imageEntity = imageEntityMapper.mapToEntity(image);

            DishEntity dish = dishEntity.get();
            dish.setImageEntity(imageEntity);
            dishJpaRepository.saveAndFlush(dish);
        }
    }

    @Override
    public Optional<Dish> permanentlyHideDishFromAllUsers(Integer dishId) {
        Optional<DishEntity> dish = dishJpaRepository.findById(dishId);
        if (dish.isPresent()) {
            DishEntity dishEntity = dish.get();
            dishEntity.setIsHidden(true);
            dishJpaRepository.saveAndFlush(dishEntity);
            return dish.map(dishEntityMapper::mapFromEntity);
        }
        return Optional.empty();
    }

    @Override
    public void permanentlyHideDishesFromAllUsers(List<Dish> dishesInOrder) {
        List<DishEntity> dishEntityList = dishesInOrder.stream()
                .map(dishEntityMapper::mapToEntity)
                .peek(dishEntity -> {
                    dishEntity.setIsHidden(true);
                    dishEntity.setDishCategory(null);
                })
                .toList();

        dishJpaRepository.saveAllAndFlush(dishEntityList);
    }

    @Override
    public List<Dish> findAllNotHiddenDishes(String restaurantName) {
        return dishJpaRepository
                .findAllByRestaurant_RestaurantNameAndRestaurant_IsShownAndIsHidden(restaurantName, true, false)
                .stream()
                .map(dishEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public List<Dish> findNotHiddenDishesByCategory(String restaurantName, String category) {
        return dishJpaRepository
                .findAllByRestaurant_RestaurantNameAndRestaurant_IsShownAndDishCategory_CategoryNameAndIsHidden(restaurantName,  true, category, false)
                .stream()
                .map(dishEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public Optional<Dish> findById(Integer dishId) {
        return dishJpaRepository.findById(dishId)
                .map(dishEntityMapper::mapFromEntity);
    }
}
