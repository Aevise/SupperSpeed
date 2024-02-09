package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.DishCategoryDTO;
import pl.Aevise.SupperSpeed.domain.DishCategory;

@Mapper(componentModel = "spring")
public interface DishCategoryMapper {

    DishCategoryDTO mapToDTO(final DishCategory dishCategory);

    DishCategory mapFromDTO(DishCategoryDTO dishCategoryDTO);
}
