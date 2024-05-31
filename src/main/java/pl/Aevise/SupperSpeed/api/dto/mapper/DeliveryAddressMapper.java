package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;

@Mapper(componentModel = "spring")
public interface DeliveryAddressMapper {
    DeliveryAddress mapFromDTO(final DeliveryAddressDTO deliveryAddressDTO);

    DeliveryAddressDTO mapToDTO(DeliveryAddress deliveryAddress);
}
