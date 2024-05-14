package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.DishCategoryMapper;
import pl.Aevise.SupperSpeed.business.dao.DishCategoryDAO;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishCategoryJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DishJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DishCategoryEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DishCategoryRepository implements DishCategoryDAO {

    private final DishCategoryJpaRepository dishCategoryJpaRepository;
    private final DishJpaRepository dishJpaRepository;
    private final DishRepository dishRepository;
    private final DishCategoryEntityMapper dishCategoryEntityMapper;
    private final DishCategoryMapper dishCategoryMapper;

    @Override
    public List<DishCategory> findAllByRestaurant(Integer restaurantId) {
        return dishCategoryJpaRepository
                .findAllByRestaurant_Id(restaurantId)
                .stream()
                .map(dishCategoryEntityMapper::mapFromEntity)
                .toList();
    }

    @Override
    public void updateCategory(DishCategoryDTO dishCategoryDTO) {
        Integer dishCategoryId = dishCategoryDTO.getDishCategoryId();
        DishCategoryEntity dishCategory = dishCategoryJpaRepository
                .findById(dishCategoryId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dish category with id: [%s] not found"
                                .formatted(dishCategoryId)
                ));
        DishCategory newDishCategoryData = dishCategoryMapper.mapFromDTO(dishCategoryDTO);

        dishCategory.setCategoryName(newDishCategoryData.getCategoryName());
        dishCategoryJpaRepository.saveAndFlush(dishCategory);
    }

    @Override
    public void deleteCategory(Integer categoryId) {
        dishCategoryJpaRepository.deleteById(categoryId);
    }

    @Override
    public void addCategory(DishCategoryEntity dishCategoryEntity) {
        dishCategoryJpaRepository.saveAndFlush(dishCategoryEntity);
    }

    @Override
    public Optional<DishCategory> findById(Integer id) {
        return dishCategoryJpaRepository.findById(id)
                .map(dishCategoryEntityMapper::mapFromEntity);
    }
}
