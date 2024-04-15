package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

@Mapper(componentModel = "spring", uses = {
        RestaurantEntityMapper.class,
        ClientEntityMapper.class,
        StatusListEntityMapper.class
})
public interface SupperOrderEntityMapper {


    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(source = "client", target = "client")
    @Mapping(source = "status", target = "status")
    SupperOrderEntity mapToEntity(SupperOrder supperOrder);

    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(source = "client", target = "client")
    @Mapping(source = "status", target = "status")
    SupperOrder mapFromEntity(SupperOrderEntity supperOrderEntity);
}
