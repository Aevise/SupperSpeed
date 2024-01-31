package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;

@Repository
public interface ClientJpaRepository extends JpaRepository<ClientEntity, Integer> {


}
