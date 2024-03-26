package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.CuisineEntity;

import java.util.Optional;

public interface CuisineJpaRepository extends JpaRepository<CuisineEntity, Integer> {

    Optional<CuisineEntity> findByCuisine(String cuisine);
}
