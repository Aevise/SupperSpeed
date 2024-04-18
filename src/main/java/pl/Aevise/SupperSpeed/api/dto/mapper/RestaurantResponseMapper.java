package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.RestaurantResponseDTO;
import pl.Aevise.SupperSpeed.domain.RestaurantResponse;

@Mapper(componentModel = "spring")
public interface RestaurantResponseMapper {

    RestaurantResponse mapFromDTO(final RestaurantResponseDTO restaurantResponseDTO);

    RestaurantResponseDTO mapToDTO(RestaurantResponse restaurantResponse);
}
