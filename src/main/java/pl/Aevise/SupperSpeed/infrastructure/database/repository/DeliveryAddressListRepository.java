package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.DeliveryAddressListDAO;
import pl.Aevise.SupperSpeed.domain.DeliveryAddress;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.DeliveryAddressListJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.DeliveryAddressListEntityMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class DeliveryAddressListRepository implements DeliveryAddressListDAO {

    private final DeliveryAddressListJpaRepository deliveryAddressListJpaRepository;

    private final DeliveryAddressListEntityMapper deliveryAddressListEntityMapper;

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
}
