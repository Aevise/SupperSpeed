package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DeliveryAddressEntity;

public interface DeliveryAddressJpaRepository extends JpaRepository<DeliveryAddressEntity, Integer> {
}
