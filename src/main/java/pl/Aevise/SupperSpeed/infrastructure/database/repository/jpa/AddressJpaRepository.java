package pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;

import java.util.List;

@Repository
public interface AddressJpaRepository extends JpaRepository<AddressEntity, Integer> {

    @Query("SELECT DISTINCT a.city FROM AddressEntity a WHERE a.city IS NOT NULL")
    List<String> findDistinctCities();
}
