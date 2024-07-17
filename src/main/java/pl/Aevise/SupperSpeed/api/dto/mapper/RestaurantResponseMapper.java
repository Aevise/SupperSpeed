package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.RestaurantResponseDTO;
import pl.Aevise.SupperSpeed.domain.RestaurantResponse;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring",
        uses = {
                OffsetDateTimeMapper.class
        })
public interface RestaurantResponseMapper {

    @Mapping(source = "responseDateTime", target = "responseDateTime", qualifiedByName = "mapStringToOffsetDateTime")
    RestaurantResponse mapFromDTO(final RestaurantResponseDTO restaurantResponseDTO);

    @Mapping(source = "responseDateTime", target = "responseDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    RestaurantResponseDTO mapToDTO(final RestaurantResponse restaurantResponse);
}
