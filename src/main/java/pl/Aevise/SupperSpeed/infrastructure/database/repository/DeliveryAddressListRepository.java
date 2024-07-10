package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressListDAO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.domain.Restaurant;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DeliveryAddressListJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DeliveryAddressEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DeliveryAddressListEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.RestaurantEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DeliveryAddressListRepository implements DeliveryAddressListDAO {

    private final DeliveryAddressListJpaRepository deliveryAddressListJpaRepository;
    private final DeliveryAddressListEntityMapper deliveryAddressListEntityMapper;

    private final DeliveryAddressEntityMapper deliveryAddressEntityMapper;

    private final RestaurantEntityMapper restaurantEntityMapper;

    @Override
    public Page<DeliveryAddressList> getAllDeliveryAddressesByRestaurantId(Integer restaurantId, PageRequest pageRequest) {
        Page<DeliveryAddressListEntity> allByRestaurantEntityId = deliveryAddressListJpaRepository.getAllByRestaurantEntity_Id(restaurantId, pageRequest);
        if (!allByRestaurantEntityId.isEmpty()) {
            return allByRestaurantEntityId
                    .map(deliveryAddressListEntityMapper::mapFromEntity);
        }
        return Page.empty();
    }

    @Override
    public void removeDeliveryAddress(DeliveryAddressKey deliveryAddressKeyId) {
        deliveryAddressListJpaRepository.deleteById(deliveryAddressKeyId);
    }

    @Override
    public void addNewRestaurantToDeliveryAddress(DeliveryAddressListEntity deliveryAddress) {
        deliveryAddressListJpaRepository.save(deliveryAddress);
    }

    @Override
    public Optional<DeliveryAddressList> getByRestaurantAndAddress(DeliveryAddressListEntity deliveryAddressListEntity) {
        Optional<DeliveryAddressListEntity> byId = deliveryAddressListJpaRepository.findById(deliveryAddressListEntity.getId());
        if (byId.isPresent()) {
            return byId.map(deliveryAddressListEntityMapper::mapFromEntity);
        }
        return Optional.empty();
    }

    @Override
    public List<DeliveryAddress> getAllDeliveryAddressesByRestaurantId(Integer restaurantId) {
        List<DeliveryAddressEntity> deliveryAddressesForRestaurant = deliveryAddressListJpaRepository.getDeliveryAddressesForRestaurant(restaurantId);

        if (!deliveryAddressesForRestaurant.isEmpty()) {
            return deliveryAddressesForRestaurant.stream()
                    .map(deliveryAddressEntityMapper::mapFromEntity)
                    .toList();
        }
        return List.of();
    }

    @Override
    public Page<Restaurant> getAllByCityAndStreetName(String city, String streetName, PageRequest pageRequest) {
        Page<RestaurantEntity> restaurants = deliveryAddressListJpaRepository.getAllRestaurantsByCityAndStreetName(city, streetName, pageRequest);

        if (!restaurants.isEmpty()) {
            return restaurants
                    .map(restaurantEntityMapper::mapFromEntity);
        }
        return Page.empty();
    }

    @Override
    public Page<Restaurant> getAllByCityAndStreetNameByCuisine(String city, String streetName, String cuisine, PageRequest pageRequest) {
        Page<RestaurantEntity> restaurants = deliveryAddressListJpaRepository.getAllRestaurantsByCityAndStreetNameAndCuisine(city, streetName, cuisine, pageRequest);

        if (!restaurants.isEmpty()) {
            return restaurants
                    .map(restaurantEntityMapper::mapFromEntity);
        }
        return Page.empty();
    }

    @Override
    public List<String> getCuisineFromRestaurantsDeliveringTo(String city, String streetName) {
        List<String> cuisines = deliveryAddressListJpaRepository.findCuisinesFromRestaurantsDeliveringTo(city, streetName);
        if (cuisines.isEmpty()) {
            return List.of();
        }
        return cuisines;
    }
}
