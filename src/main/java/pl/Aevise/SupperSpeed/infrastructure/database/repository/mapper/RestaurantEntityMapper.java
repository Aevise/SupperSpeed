package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantEntityMapper {


    @Mapping(target = "supperUser", ignore = true)
    @Mapping(source = "id", target = "restaurantId")
    Restaurant mapFromEntity(RestaurantEntity restaurantEntity);

    @Mapping(source = "restaurantId", target = "id")
    RestaurantEntity mapToEntity(Restaurant restaurant);
}
