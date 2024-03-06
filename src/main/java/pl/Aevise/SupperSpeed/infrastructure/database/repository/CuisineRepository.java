package pl.Aevise.SupperSpeed.infrastructure.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.business.dao.CuisineDAO;
import pl.Aevise.SupperSpeed.domain.Cuisine;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.CuisineJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper.CuisineEntityMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class CuisineRepository implements CuisineDAO {

    private final CuisineJpaRepository cuisineJpaRepository;
    private final CuisineEntityMapper cuisineEntityMapper;

    @Override
    public List<Cuisine> findAll() {
        return cuisineJpaRepository
                .findAll()
                .stream()
                .map(cuisineEntityMapper::mapFromEntity)
                .toList();
    }
}
