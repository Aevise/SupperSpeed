package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring", uses = {
        RestaurantEntityMapper.class,
        DeliveryAddressEntityMapper.class
})
public interface DeliveryAddressListEntityMapper {

    @Mapping(source = "restaurantEntity.id", target = "restaurantId")
    @Mapping(source = "deliveryAddressEntity", target = "deliveryAddress")
    DeliveryAddressList mapFromEntity(DeliveryAddressListEntity deliveryAddressEntity);

    @Mapping(source = "deliveryAddress", target = "deliveryAddressEntity")
    @Mapping(source = "restaurantId", target = "restaurantEntity.id")
    DeliveryAddressListEntity mapToEntity(DeliveryAddressList deliveryAddressList);
}
