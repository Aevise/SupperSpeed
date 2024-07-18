package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;

import java.util.Optional;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientEntity, Integer> {
    Optional<ClientEntity> findBySupperUser_SupperUserId(Integer userId);

    Optional<ClientEntity> findBySupperUser_Email(String email);
}
