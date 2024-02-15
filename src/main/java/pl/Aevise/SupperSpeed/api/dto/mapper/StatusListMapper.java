package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.StatusListDTO;
import pl.Aevise.SupperSpeed.domain.StatusList;

@Mapper(componentModel = "spring")
public interface StatusListMapper {

    StatusListDTO mapToDTO(final StatusList statusList);

    StatusList mapFromDTO(StatusListDTO statusListDTO);
}
