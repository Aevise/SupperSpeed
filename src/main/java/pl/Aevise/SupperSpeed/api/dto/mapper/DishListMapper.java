package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.DishListDTO;
import pl.Aevise.SupperSpeed.domain.DishList;

@Mapper(componentModel = "spring", uses = {
        SupperOrderMapper.class,
        DishMapper.class
})
public interface DishListMapper {

    @Mapping(target = "dishDTO", source = "dish")
    @Mapping(target = "supperOrderDTO", source = "supperOrder")
    DishListDTO mapToDTO(final DishList dishList);

    @Mapping(target = "dish", source = "dishDTO")
    @Mapping(target = "supperOrder", source = "supperOrderDTO")
    DishList mapFromDTO(DishListDTO dishListDTO);
}
