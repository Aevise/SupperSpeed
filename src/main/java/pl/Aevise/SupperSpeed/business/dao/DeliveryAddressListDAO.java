package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

import java.util.List;

public interface DeliveryAddressListDAO {
    List<DeliveryAddressList> getAllByRestaurantId(Integer restaurantId);

    void deleteByAddressAndRestaurantId(DeliveryAddressKey deliveryAddressKey);

    void addNewRestaurantToDeliveryAddress(DeliveryAddressListEntity deliveryAddress);
}
