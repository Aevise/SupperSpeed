package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.StatusListDTO;
import pl.Aevise.SupperSpeed.domain.StatusList;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface StatusListMapper {

    StatusListDTO mapToDTO(final StatusList statusList);

    StatusList mapFromDTO(StatusListDTO statusListDTO);
}
