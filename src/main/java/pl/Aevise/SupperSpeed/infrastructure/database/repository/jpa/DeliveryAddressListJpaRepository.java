package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

import java.util.List;

public interface DeliveryAddressListJpaRepository extends JpaRepository<DeliveryAddressListEntity, DeliveryAddressKey> {

    List<DeliveryAddressListEntity> getAllByRestaurantEntity_Id(Integer restaurantId);
}
