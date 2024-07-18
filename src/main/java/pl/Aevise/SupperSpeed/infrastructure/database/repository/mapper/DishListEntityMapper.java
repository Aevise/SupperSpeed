package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.DishList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishesListEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring", uses = {
        DishEntityMapper.class,
        SupperOrderEntityMapper.class
})
public interface DishListEntityMapper {

    @Mapping(source = "order", target = "supperOrder")
    @Mapping(source = "dish", target = "dish")
    DishList mapFromEntity(DishesListEntity dishesListEntity);

    @Mapping(source = "supperOrder", target = "order")
    @Mapping(source = "dish", target = "dish")
    DishesListEntity mapToEntity(DishList dish);
}
