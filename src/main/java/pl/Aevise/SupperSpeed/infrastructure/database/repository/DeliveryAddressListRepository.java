package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressListDAO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DeliveryAddressListJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DeliveryAddressEntityMapper;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DeliveryAddressListEntityMapper;

import java.util.List;
import java.util.Optional;

@Repository
@AllArgsConstructor
public class DeliveryAddressListRepository implements DeliveryAddressListDAO {

    private final DeliveryAddressListJpaRepository deliveryAddressListJpaRepository;
    private final DeliveryAddressListEntityMapper deliveryAddressListEntityMapper;

    private final DeliveryAddressEntityMapper deliveryAddressEntityMapper;

    @Override
    public List<DeliveryAddressList> getAllByRestaurantId(Integer restaurantId) {
        List<DeliveryAddressListEntity> allByRestaurantEntityId = deliveryAddressListJpaRepository.getAllByRestaurantEntity_Id(restaurantId);
        if(!allByRestaurantEntityId.isEmpty()){
                return allByRestaurantEntityId.stream()
                        .map(deliveryAddressListEntityMapper::mapFromEntity)
                        .toList();
        }
        return List.of();
    }

    @Override
    public void deleteByAddressAndRestaurantId(DeliveryAddressKey deliveryAddressKeyId) {
        deliveryAddressListJpaRepository.deleteById(deliveryAddressKeyId);
    }

    @Override
    public void addNewRestaurantToDeliveryAddress(DeliveryAddressListEntity deliveryAddress) {
        deliveryAddressListJpaRepository.save(deliveryAddress);
    }

    @Override
    public Optional<DeliveryAddressList> getByRestaurantAndAddress(DeliveryAddressListEntity deliveryAddressListEntity) {
        Optional<DeliveryAddressListEntity> byId = deliveryAddressListJpaRepository.findById(deliveryAddressListEntity.getId());
        if(byId.isPresent()){
            return byId.map(deliveryAddressListEntityMapper::mapFromEntity);
        }
        return Optional.empty();
    }

    @Override
    public void test(String s, PageRequest deliveryAddressEntity) {
        List<DeliveryAddressListEntity> allByDeliveryAddressEntityPostalCodeEquals = deliveryAddressListJpaRepository.getAllByDeliveryAddressEntity_PostalCodeEquals(s, deliveryAddressEntity);
        System.out.println("");
    }

    @Override
    public List<DeliveryAddress> getAddressesWithoutDeliveryBasedOnPostalCode(Integer restaurantId, String postalCode) {
        return deliveryAddressListJpaRepository
                .getAddressesWithoutDeliveryBasedOnPostalCode(restaurantId, postalCode)
                .stream()
                .map(deliveryAddressEntityMapper::mapFromEntity)
                .toList();
    }
}
