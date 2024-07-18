package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface ClientEntityMapper {
    Client mapFromEntity(ClientEntity clientEntity);

    @Mapping(target = "address", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "supperUser", ignore = true)
    ClientEntity mapToEntity(Client client);
}
