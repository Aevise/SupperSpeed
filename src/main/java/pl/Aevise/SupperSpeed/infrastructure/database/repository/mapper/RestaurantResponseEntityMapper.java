package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.RestaurantResponse;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface RestaurantResponseEntityMapper {

    RestaurantResponseEntity mapToEntity(RestaurantResponse restaurantResponse);

    RestaurantResponse mapFromEntity(RestaurantResponseEntity restaurantResponseEntity);
}
