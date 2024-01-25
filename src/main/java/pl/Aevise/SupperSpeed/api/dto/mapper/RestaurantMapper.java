package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.domain.Restaurant;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    RestaurantDTO map(final Restaurant restaurant);

}
