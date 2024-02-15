package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.SupperOrderDTO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;

@Mapper(componentModel = "spring")
public interface SupperOrderMapper {

    SupperOrderDTO mapToDTO(final SupperOrder supperOrder);

    SupperOrder mapFromDTO(SupperOrderDTO supperOrderDTO);
}
