package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

import java.awt.print.Pageable;
import java.util.List;

public interface DeliveryAddressListJpaRepository extends JpaRepository<DeliveryAddressListEntity, DeliveryAddressKey> {

    List<DeliveryAddressListEntity> getAllByRestaurantEntity_Id(Integer restaurantId);

    List<DeliveryAddressListEntity> getAllByDeliveryAddressEntity_PostalCodeEquals(String postalCode, PageRequest pageable);
}