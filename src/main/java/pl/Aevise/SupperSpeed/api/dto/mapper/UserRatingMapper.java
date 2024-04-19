package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.domain.UserRating;

@Mapper(componentModel = "spring",
        uses = {
                RestaurantResponseMapper.class
        })
public interface UserRatingMapper {

    @Mapping(source = "restaurantResponse", target = "restaurantResponseDTO")
    UserRatingDTO mapToDTO(final UserRating userRating);

    @Mapping(source = "restaurantResponseDTO", target = "restaurantResponse")
    UserRating mapFromDTO(UserRatingDTO userRatingDTO);
}
