package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressDAO {
    void deleteDeliveryAddressById(Integer deliveryAddressId);

    Optional<DeliveryAddress> findById(Integer deliveryAddressId);

    Optional<DeliveryAddress> checkIfDeliveryAddressExist(DeliveryAddress deliveryAddress);

    DeliveryAddress saveNewDeliveryAddress(DeliveryAddress newDeliveryAddress);

    List<DeliveryAddress> getAllByPostalCode(String postalCode);

}
