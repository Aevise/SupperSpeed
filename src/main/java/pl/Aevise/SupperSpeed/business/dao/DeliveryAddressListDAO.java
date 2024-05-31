package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;

import java.util.List;

public interface DeliveryAddressListDAO {
    List<DeliveryAddressList> getAllByRestaurantId(Integer restaurantId);
}
