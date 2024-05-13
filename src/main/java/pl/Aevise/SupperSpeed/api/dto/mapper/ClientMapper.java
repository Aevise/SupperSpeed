package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;
import pl.Aevise.SupperSpeed.domain.Client;

@Mapper(componentModel = "spring",
        uses = {
                AddressMapper.class
        })
public interface ClientMapper {
    @Mapping(source = "supperUser.supperUserId", target = "supperUserId")
    ClientDTO mapToDTO(final Client client);

    Client mapFromDTO(final ClientDTO clientDTO);
}
