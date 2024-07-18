package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring", uses = {
        RestaurantEntityMapper.class,
        ClientEntityMapper.class,
        StatusListEntityMapper.class,
        UserRatingEntityMapper.class
})
public interface SupperOrderEntityMapper {


    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(source = "client", target = "client")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userRating", target = "userRating")
    SupperOrderEntity mapToEntity(SupperOrder supperOrder);

    @Mapping(source = "restaurant", target = "restaurant")
    @Mapping(source = "client", target = "client")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "userRating", target = "userRating")
    SupperOrder mapFromEntity(SupperOrderEntity supperOrderEntity);
}
