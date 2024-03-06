package pl.Aevise.SupperSpeed.infrastructure.security.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.security.dto.SupperUserDTO;

@Mapper(componentModel = "spring")
public interface SupperUserMapper {

    @Mapping(target = "password", ignore = true)
    SupperUser mapFromDTO(final SupperUserDTO supperUserDTO);

    SupperUserDTO mapToDTO(SupperUser supperUser);
}
