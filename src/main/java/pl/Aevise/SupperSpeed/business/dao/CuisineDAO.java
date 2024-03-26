package pl.Aevise.SupperSpeed.business.dao;

import pl.Aevise.SupperSpeed.domain.Cuisine;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.CuisineEntity;

import java.util.List;
import java.util.Optional;

public interface CuisineDAO {
    List<Cuisine> findAll();

    Optional<CuisineEntity> findByCuisineName(String cuisine);
}
