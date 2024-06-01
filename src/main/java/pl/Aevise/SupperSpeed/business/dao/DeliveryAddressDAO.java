package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.DeliveryAddress;

import java.util.Optional;

public interface DeliveryAddressDAO {
    void deleteDeliveryAddressById(Integer deliveryAddressId);

    Optional<DeliveryAddress> findById(Integer deliveryAddressId);
}
