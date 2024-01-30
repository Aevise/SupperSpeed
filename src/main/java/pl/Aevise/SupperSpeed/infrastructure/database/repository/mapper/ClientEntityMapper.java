package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;

@Mapper(componentModel = "spring")
public interface ClientEntityMapper {
    Client mapFromEntity(ClientEntity clientEntity);
}
