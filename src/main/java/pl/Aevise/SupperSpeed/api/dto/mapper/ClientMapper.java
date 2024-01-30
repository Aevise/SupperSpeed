package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.domain.Client;

@Mapper(componentModel = "spring")
public interface ClientMapper {
    ClientDTO map(final Client client);
}
