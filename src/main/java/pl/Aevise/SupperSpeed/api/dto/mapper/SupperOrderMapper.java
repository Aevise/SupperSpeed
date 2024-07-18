package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.RestOrderDTO;
import pl.Aevise.SupperSpeed.api.dto.SupperOrderDTO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring", uses = {
        OffsetDateTimeMapper.class,
        ClientMapper.class,
        RestaurantMapper.class,
        StatusListMapper.class,
        UserRatingMapper.class
})
public interface SupperOrderMapper {

    @Mapping(source = "orderDateTime", target = "orderDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    @Mapping(source = "client", target = "clientDTO")
    @Mapping(source = "restaurant", target = "restaurantDTO")
    @Mapping(source = "status", target = "statusListDTO")
    @Mapping(source = "userRating", target = "userRatingDTO")
    SupperOrderDTO mapToDTO(final SupperOrder supperOrder);

    @Mapping(source = "orderDateTime", target = "orderDateTime", qualifiedByName = "mapStringToOffsetDateTime")
    SupperOrder mapFromDTO(SupperOrderDTO supperOrderDTO);

    @Mapping(source = "restaurantDTO.restaurantName", target = "restaurantName")
    @Mapping(source = "statusListDTO.description", target = "status")
    @Mapping(source = "userRatingDTO", target = "userRating")
    RestOrderDTO mapToRestDTO(SupperOrderDTO supperOrderDTO);
}
