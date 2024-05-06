package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ImageEntity;

public interface LogoJpaRepository extends JpaRepository<ImageEntity, Integer> {
}
