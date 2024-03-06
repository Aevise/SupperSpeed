package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.StatusList;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.StatusListEntity;

@Mapper(componentModel = "spring")
public interface StatusListEntityMapper {

    StatusListEntity mapToEntity(StatusList statusList);

    StatusList mapFromEntity(StatusListEntity statusList);
}
