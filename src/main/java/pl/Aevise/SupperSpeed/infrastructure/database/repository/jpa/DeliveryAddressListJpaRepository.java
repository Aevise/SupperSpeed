package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface DeliveryAddressListJpaRepository extends JpaRepository<DeliveryAddressListEntity, DeliveryAddressKey> {

    List<DeliveryAddressListEntity> getAllByRestaurantEntity_Id(Integer restaurantId);

    List<DeliveryAddressListEntity> getAllByDeliveryAddressEntity_PostalCodeEquals(String postalCode, PageRequest pageable);

    @Query("SELECT r.deliveryAddressEntity FROM DeliveryAddressListEntity r WHERE r.restaurantEntity.id <> :restaurantId AND r.deliveryAddressEntity.postalCode = :postalCode")
    List<DeliveryAddressEntity> getAddressesWithoutDeliveryBasedOnPostalCode(@Param("restaurantId") Integer restaurantId, @Param("postalCode") String postalCode);

}