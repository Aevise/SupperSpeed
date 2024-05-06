package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.domain.Restaurant;

@Mapper(componentModel = "spring",
        uses = {
                CuisineMapper.class,
                ImageMapper.class
        })
public interface RestaurantMapper {

    @Mapping(source = "image", target = "imageDTO")
    RestaurantDTO mapToDTO(final Restaurant restaurant);

    @Mapping(source = "imageDTO", target = "image")
    Restaurant mapFromDTO(RestaurantDTO restaurantDTO);
}
