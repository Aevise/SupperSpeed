package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.domain.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDTO mapToDTO(final Address address);

    Address mapFromDTO(final AddressDTO addressDTO);
}
