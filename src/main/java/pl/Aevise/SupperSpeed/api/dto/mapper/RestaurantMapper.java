package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.domain.Restaurant;

@Mapper(componentModel = "spring", uses = {CuisineMapper.class, LogoMapper.class})
public interface RestaurantMapper {

    @Mapping(target = "cuisine", ignore = true)
    @Mapping(target = "logo", ignore = true)
    RestaurantDTO mapToDTO(final Restaurant restaurant);

    @Mapping(target = "cuisine", ignore = true)
    @Mapping(target = "logo", ignore = true)
    Restaurant mapFromDTO(RestaurantDTO restaurantDTO);
}
