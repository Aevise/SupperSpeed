package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.DeliveryAddress;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface DeliveryAddressDAO {
    void deleteDeliveryAddressById(Integer deliveryAddressId);

    Optional<DeliveryAddress> findById(Integer deliveryAddressId);

    Optional<DeliveryAddress> checkIfDeliveryAddressExist(DeliveryAddress deliveryAddress);

    DeliveryAddress saveNewDeliveryAddress(DeliveryAddress newDeliveryAddress);
}
