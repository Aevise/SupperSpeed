package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.domain.Dish;

@Mapper(componentModel = "spring")
public interface DishMapper {

    Dish mapFromDTO(final DishDTO dishDTO);

    DishDTO mapToDTO(Dish dish);
}
