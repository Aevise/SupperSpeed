package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;

@Mapper(componentModel = "spring")
public interface DishCategoryEntityMapper {

    DishCategory mapFromEntity(DishCategoryEntity dishCategoryEntity);
}
