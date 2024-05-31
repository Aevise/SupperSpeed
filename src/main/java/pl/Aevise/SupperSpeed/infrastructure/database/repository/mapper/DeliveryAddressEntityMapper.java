package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DeliveryAddressEntityMapper {

    @Mapping(source = "deliveryAddressEntity", target = "DeliveryAddress")
    DeliveryAddress mapFromEntity(DeliveryAddressEntity deliveryAddressEntity);

    @Mapping(source = "DeliveryAddress", target = "deliveryAddressEntity")
    DeliveryAddressEntity mapToEntity(DeliveryAddress deliveryAddress);
}
