package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.UserRating;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring",
        uses = {
                RestaurantResponseEntityMapper.class
        })
public interface UserRatingEntityMapper {

    @Mapping(source = "restaurantResponse", target = "restaurantResponseEntity")
    UserRatingEntity mapToEntity(UserRating userRating);

    @Mapping(source = "restaurantResponseEntity", target = "restaurantResponse")
    UserRating mapFromEntity(UserRatingEntity userRatingEntity);
}
