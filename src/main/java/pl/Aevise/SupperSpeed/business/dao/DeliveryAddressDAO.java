package pl.Aevise.SupperSpeed.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressDAO {
    void deleteDeliveryAddressById(Integer deliveryAddressId);

    Optional<DeliveryAddress> findById(Integer deliveryAddressId);

    Optional<DeliveryAddress> checkIfDeliveryAddressExist(DeliveryAddress deliveryAddress);

    DeliveryAddress saveNewDeliveryAddress(DeliveryAddress newDeliveryAddress);

    List<DeliveryAddress> getAllByPostalCode(String postalCode);
}
