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
}
