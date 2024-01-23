package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;

public interface UserRatingJpaRepository extends JpaRepository<UserRatingEntity, Integer> {
}
