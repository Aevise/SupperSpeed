package pl.Aevise.SupperSpeed.business.dao;

import org.springframework.data.domain.PageRequest;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressListDAO {
    List<DeliveryAddressList> getAllByRestaurantId(Integer restaurantId);

    void deleteByAddressAndRestaurantId(DeliveryAddressKey deliveryAddressKey);

    void addNewRestaurantToDeliveryAddress(DeliveryAddressListEntity deliveryAddress);

    Optional<DeliveryAddressList> getByRestaurantAndAddress(DeliveryAddressListEntity deliveryAddressListEntity);

    void test(String s, PageRequest deliveryAddressEntity);

    List<DeliveryAddress> getAddressesWithoutDeliveryBasedOnPostalCode(Integer restaurantId, String postalCode);
}
