package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.CuisineDTO;
import pl.Aevise.SupperSpeed.domain.Cuisine;

@Mapper(componentModel = "spring")
public interface CuisineMapper {

    Cuisine mapFromDTO(final CuisineDTO cuisineDTO);

    CuisineDTO mapToDTO(Cuisine cuisine);
}