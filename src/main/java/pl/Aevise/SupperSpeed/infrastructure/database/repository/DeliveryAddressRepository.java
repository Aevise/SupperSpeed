package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressDAO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DeliveryAddressJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DeliveryAddressEntityMapper;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class DeliveryAddressRepository implements DeliveryAddressDAO {

    private final DeliveryAddressJpaRepository deliveryAddressJpaRepository;
    private final DeliveryAddressEntityMapper deliveryAddressEntityMapper;

    @Override
    public void deleteDeliveryAddressById(Integer deliveryAddressId) {
            deliveryAddressJpaRepository.deleteById(deliveryAddressId);
    }

    @Override
    public Optional<DeliveryAddress> findById(Integer deliveryAddressId) {
        Optional<DeliveryAddressEntity> byId = deliveryAddressJpaRepository.findById(deliveryAddressId);

        if(byId.isPresent()){
            return byId.map(deliveryAddressEntityMapper::mapFromEntity);
        }
        return Optional.empty();
    }

    @Override
    public Optional<DeliveryAddress> checkIfDeliveryAddressExist(DeliveryAddress deliveryAddress) {
        Optional<DeliveryAddressEntity> byAllFieldsExceptId = deliveryAddressJpaRepository.findByAllFieldsExceptId(
                deliveryAddress.getCountry(),
                deliveryAddress.getCity(),
                deliveryAddress.getDistrict(),
                deliveryAddress.getPostalCode(),
                deliveryAddress.getStreetName()
        );

        if(byAllFieldsExceptId.isPresent()){
            return byAllFieldsExceptId.map(deliveryAddressEntityMapper::mapFromEntity);
        }
        return Optional.empty();
    }

    @Override
    public DeliveryAddress saveNewDeliveryAddress(DeliveryAddress newDeliveryAddress) {
        DeliveryAddressEntity deliveryAddressEntity = deliveryAddressJpaRepository.saveAndFlush(
                deliveryAddressEntityMapper.mapToEntity(newDeliveryAddress)
        );
        return deliveryAddressEntityMapper.mapFromEntity(deliveryAddressEntity);
    }
}
