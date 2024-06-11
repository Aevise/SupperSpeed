package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressListDAO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DeliveryAddressListJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DeliveryAddressEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DeliveryAddressListEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
@AllArgsConstructor
public class DeliveryAddressListRepository implements DeliveryAddressListDAO {

    private final DeliveryAddressListJpaRepository deliveryAddressListJpaRepository;
    private final DeliveryAddressListEntityMapper deliveryAddressListEntityMapper;

    private final DeliveryAddressEntityMapper deliveryAddressEntityMapper;

    @Override
    public Page<DeliveryAddressList> getAllByRestaurantId(Integer restaurantId, PageRequest pageRequest) {
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
    public void test(String postalCode, PageRequest deliveryAddressEntity) {
        Page<DeliveryAddressListEntity> allByDeliveryAddressEntityPostalCodeEquals = deliveryAddressListJpaRepository.getAllByDeliveryAddressEntity_PostalCodeEquals(postalCode, deliveryAddressEntity);
        List<DeliveryAddressListEntity> deliveryAddressListEntityStream = allByDeliveryAddressEntityPostalCodeEquals.get().toList();
        int totalPages = allByDeliveryAddressEntityPostalCodeEquals.getTotalPages();
        System.out.println("");
    }

    @Override
    public List<DeliveryAddress> getAddressesWithoutDeliveryBasedOnPostalCode(Integer restaurantId, DeliveryAddress deliveryAddress) {

        DeliveryAddressEntity newDeliveryAddress = deliveryAddressEntityMapper.mapToEntity(deliveryAddress);
        List<DeliveryAddressEntity> deliveryAddressesForRestaurant = deliveryAddressListJpaRepository.getDeliveryAddressesForRestaurant(restaurantId);

        return deliveryAddressListJpaRepository
                .getAddressesWithoutDeliveryBasedOnPostalCode(restaurantId, newDeliveryAddress.getPostalCode())
                .stream()
                .filter(currentAddress -> !deliveryAddressesForRestaurant.contains(currentAddress))
                .map(deliveryAddressEntityMapper::mapFromEntity)
                .toList();
    }
}
