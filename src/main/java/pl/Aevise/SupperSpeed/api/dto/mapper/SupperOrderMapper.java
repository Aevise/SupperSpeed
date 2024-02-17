package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.SupperOrderDTO;
import pl.Aevise.SupperSpeed.domain.SupperOrder;

@Mapper(componentModel = "spring", uses = OffsetDateTimeMapper.class)
public interface SupperOrderMapper {

    @Mapping(source = "orderDateTime", target = "orderDateTime", qualifiedByName = "mapOffsetDateTimeToString")
    SupperOrderDTO mapToDTO(final SupperOrder supperOrder);

    @Mapping(source = "orderDateTime", target = "orderDateTime", qualifiedByName = "mapStringToOffsetDateTime")
    SupperOrder mapFromDTO(SupperOrderDTO supperOrderDTO);
}
