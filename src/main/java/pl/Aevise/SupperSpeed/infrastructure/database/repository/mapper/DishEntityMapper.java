package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.Dish;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;

@Mapper(componentModel = "spring",
        uses = {
                ImageEntityMapper.class
        })
public interface DishEntityMapper {

    @Mapping(source = "imageEntity", target = "image")
    Dish mapFromEntity(DishEntity dishEntity);

    @Mapping(source = "image", target = "imageEntity")
    DishEntity mapToEntity(Dish dish);
}
