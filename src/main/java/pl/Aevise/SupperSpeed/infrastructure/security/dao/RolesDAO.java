package pl.Aevise.SupperSpeed.infrastructure.security.dao;

import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;

import java.util.Optional;

public interface RolesDAO {

    Optional<RolesEntity> findById(Integer roleId);
}
