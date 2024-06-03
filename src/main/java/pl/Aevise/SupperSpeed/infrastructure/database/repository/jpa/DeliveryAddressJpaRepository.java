package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;

import java.util.Optional;

public interface DeliveryAddressJpaRepository extends JpaRepository<DeliveryAddressEntity, Integer> {
    @Query("SELECT d FROM DeliveryAddressEntity d WHERE d.country = :country AND d.city = :city AND d.district = :district AND d.postalCode = :postalCode AND d.streetName = :streetName")
    Optional<DeliveryAddressEntity> findByAllFieldsExceptId(@Param("country") String country,
                                                           @Param("city") String city,
                                                           @Param("district") String district,
                                                           @Param("postalCode") String postalCode,
                                                           @Param("streetName") String streetName);
}

