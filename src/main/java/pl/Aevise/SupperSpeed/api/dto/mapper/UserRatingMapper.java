package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring",
        uses = {
                OffsetDateTimeMapper.class,
                RestaurantResponseMapper.class
        })
public interface UserRatingMapper {

    @Mapping(source = "restaurantResponse", target = "restaurantResponseDTO")
    @Mapping(source = "ratingDateTime", target = "ratingDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    UserRatingDTO mapToDTO(final UserRating userRating);

    @Mapping(source = "restaurantResponseDTO", target = "restaurantResponse")
    @Mapping(source = "ratingDateTime", target = "ratingDateTime", qualifiedByName = "mapStringToOffsetDateTime")
    UserRating mapFromDTO(final UserRatingDTO userRatingDTO);
}
