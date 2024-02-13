package pl.Aevise.SupperSpeed.infrastructure.security.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;

@Mapper(componentModel = "spring")
public interface SupperUserEntityMapper {

    @Mapping(target = "password", ignore = true)
    SupperUser mapFromEntity(SupperUserEntity entity);
}
