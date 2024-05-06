package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.LogoDTO;
import pl.Aevise.SupperSpeed.domain.Logo;

@Mapper(componentModel = "spring")
public interface LogoMapper {

    LogoDTO mapToDTO(final Logo logo);

    Logo mapFromDTO(final LogoDTO logo);
}
