package pl.Aevise.SupperSpeed.business.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressListDAO {
    Page<DeliveryAddressList> getAllDeliveryAddressesByRestaurantId(Integer restaurantId, PageRequest pageRequest);

    void removeDeliveryAddress(DeliveryAddressKey deliveryAddressKey);

    void addNewRestaurantToDeliveryAddress(DeliveryAddressListEntity deliveryAddress);

    Optional<DeliveryAddressList> getByRestaurantAndAddress(DeliveryAddressListEntity deliveryAddressListEntity);

    List<DeliveryAddress> getAllDeliveryAddressesByRestaurantId(Integer restaurantId);

    Page<Restaurant> getAllByCityAndStreetName(String city, String streetName, PageRequest pageRequest);

    Page<Restaurant> getAllByCityAndStreetNameByCuisine(String city, String streetName, String cuisine, PageRequest pageRequest);

    List<String> getCuisineFromRestaurantsDeliveringTo(String city, String streetName);
}
