package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.LogoEntity;

public interface LogoJpaRepository extends JpaRepository<LogoEntity, Integer> {
}
