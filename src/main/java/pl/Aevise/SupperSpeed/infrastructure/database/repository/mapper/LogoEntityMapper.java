package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.Aevise.SupperSpeed.domain.Logo;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.LogoEntity;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LogoEntityMapper {

    Logo mapFromEntity(LogoEntity logoEntity);

    LogoEntity mapToEntity(Logo logo);
}
