package pl.Aevise.SupperSpeed.infrastructure.security.database.repository.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.Role;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;

@Mapper(componentModel = "spring")
public interface RoleEntityMapper {

    Role mapFromEntity(RolesEntity rolesEntity);
}
