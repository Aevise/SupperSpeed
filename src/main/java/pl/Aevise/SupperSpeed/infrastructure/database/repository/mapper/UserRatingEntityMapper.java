package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;

@Mapper(componentModel = "spring")
public interface UserRatingEntityMapper {

    UserRatingEntity mapToEntity(UserRating userRating);

    UserRating mapFromEntity(UserRatingEntity userRatingEntity);
}
