package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;

@Mapper(componentModel = "spring")
public interface DishEntityMapper {
    Dish mapFromEntity(DishEntity dishEntity);
    DishEntity mapToEntity(Dish dish);
}
