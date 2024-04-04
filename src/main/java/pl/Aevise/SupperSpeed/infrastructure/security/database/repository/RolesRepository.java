package pl.Aevise.SupperSpeed.infrastructure.security.database.repository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import pl.Aevise.SupperSpeed.infrastructure.security.dao.RolesDAO;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.jpa.RolesJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.security.database.repository.mapper.RoleEntityMapper;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class RolesRepository implements RolesDAO {

    private final RolesJpaRepository rolesJpaRepository;
    private final RoleEntityMapper roleEntityMapper;

    @Override
    public Optional<RolesEntity> findById(Integer roleId) {
        return rolesJpaRepository.findById(roleId);
    }
}
