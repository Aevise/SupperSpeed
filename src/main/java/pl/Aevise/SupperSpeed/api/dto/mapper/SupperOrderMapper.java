package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.SupperOrderDTO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;

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
}
