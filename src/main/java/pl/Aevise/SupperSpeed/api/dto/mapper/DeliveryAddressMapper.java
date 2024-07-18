package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface DeliveryAddressMapper {
    DeliveryAddress mapFromDTO(final DeliveryAddressDTO deliveryAddressDTO);

    DeliveryAddressDTO mapToDTO(DeliveryAddress deliveryAddress);
}
