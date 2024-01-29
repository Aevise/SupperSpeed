package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.domain.Restaurant;

@Mapper(componentModel = "spring", uses = {CuisineMapper.class, LogoMapper.class})
public interface RestaurantMapper {

    @Mapping(source = "cuisine", target = "cuisine")
    @Mapping(source = "logo", target = "logo")
    RestaurantDTO map(final Restaurant restaurant);

}
