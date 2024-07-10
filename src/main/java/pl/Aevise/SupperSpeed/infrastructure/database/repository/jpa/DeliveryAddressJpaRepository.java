package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;

import java.util.List;
import java.util.Optional;

public interface DeliveryAddressJpaRepository extends JpaRepository<DeliveryAddressEntity, Integer> {

    @Query("SELECT d FROM DeliveryAddressEntity d WHERE LOWER(d.country) = LOWER(:country) AND LOWER(d.city) = LOWER(:city) AND LOWER(d.district) = LOWER(:district) AND LOWER(d.postalCode) = LOWER(:postalCode) AND LOWER(d.streetName) = LOWER(:streetName)")
    Optional<DeliveryAddressEntity> findByAllFieldsExceptId(@Param("country") String country,
                                                            @Param("city") String city,
                                                            @Param("district") String district,
                                                            @Param("postalCode") String postalCode,
                                                            @Param("streetName") String streetName);

    List<DeliveryAddressEntity> findAllByPostalCodeEquals(String postalCode);


}

