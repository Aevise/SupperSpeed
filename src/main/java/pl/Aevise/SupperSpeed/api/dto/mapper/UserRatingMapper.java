package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.domain.UserRating;

@Mapper(componentModel = "spring")
public interface UserRatingMapper {

    UserRatingDTO mapToDTO(final UserRating userRating);

    UserRating mapFromDTO(UserRatingDTO userRatingDTO);
}
