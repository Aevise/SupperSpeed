package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.SupperOrder;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.SupperOrderEntity;

@Mapper(componentModel = "spring")
public interface SupperOrderEntityMapper {

    SupperOrderEntity mapToEntity(SupperOrder supperOrder);

    SupperOrder mapFromEntity(SupperOrderEntity supperOrderEntity);
}
