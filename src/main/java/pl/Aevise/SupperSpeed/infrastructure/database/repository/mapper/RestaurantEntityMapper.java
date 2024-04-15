package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {
                AddressEntityMapper.class,
                ClientEntityMapper.class
        }
)
public interface RestaurantEntityMapper {


    @Mapping(target = "supperUser", ignore = true)
    @Mapping(source = "id", target = "restaurantId")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "cuisine", target = "cuisine")
    Restaurant mapFromEntity(RestaurantEntity restaurantEntity);

    @Mapping(source = "restaurantId", target = "id")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "cuisine", target = "cuisine")
    RestaurantEntity mapToEntity(Restaurant restaurant);
}
