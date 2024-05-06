package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.domain.Restaurant;

@Mapper(componentModel = "spring",
        uses = {
                CuisineMapper.class,
                LogoMapper.class
        })
public interface RestaurantMapper {

    RestaurantDTO mapToDTO(final Restaurant restaurant);

    Restaurant mapFromDTO(RestaurantDTO restaurantDTO);
}
