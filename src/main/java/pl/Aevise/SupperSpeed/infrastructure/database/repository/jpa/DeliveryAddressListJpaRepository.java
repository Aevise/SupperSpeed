package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

import java.util.List;

public interface DeliveryAddressListJpaRepository extends JpaRepository<DeliveryAddressListEntity, DeliveryAddressKey> {

    @Query("SELECT dale FROM DeliveryAddressListEntity dale JOIN FETCH dale.deliveryAddressEntity WHERE dale.restaurantEntity.id = :restaurantId")
    Page<DeliveryAddressListEntity> getAllByRestaurantEntity_Id(@Param("restaurantId")Integer restaurantId, PageRequest pageRequest);

    Page<DeliveryAddressListEntity> getAllByDeliveryAddressEntity_PostalCodeEquals(String postalCode, PageRequest pageable);

    @Query("SELECT r.deliveryAddressEntity FROM DeliveryAddressListEntity r WHERE r.restaurantEntity.id <> :restaurantId AND r.deliveryAddressEntity.postalCode = :postalCode")
    List<DeliveryAddressEntity> getAddressesWithoutDeliveryBasedOnPostalCode(@Param("restaurantId") Integer restaurantId, @Param("postalCode") String postalCode);


    @Query("SELECT r.deliveryAddressEntity FROM DeliveryAddressListEntity r WHERE r.restaurantEntity.id = :restaurantId")
    List<DeliveryAddressEntity> getDeliveryAddressesForRestaurant(@Param("restaurantId") Integer restaurantId);


}