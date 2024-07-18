package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.DishCategory;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface DishCategoryEntityMapper {

    DishCategory mapFromEntity(DishCategoryEntity dishCategoryEntity);

    DishCategoryEntity mapToEntity(DishCategory dishCategory);
}
