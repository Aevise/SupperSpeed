package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.DishDTO;
import pl.Aevise.SupperSpeed.domain.Dish;

@Mapper(componentModel = "spring",
uses = {
        ImageMapper.class
})
public interface DishMapper {

    @Mapping(source = "imageDTO", target = "image")
    Dish mapFromDTO(final DishDTO dishDTO);

    @Mapping(source = "image", target = "imageDTO")
    DishDTO mapToDTO(Dish dish);
}
