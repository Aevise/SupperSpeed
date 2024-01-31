package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;

@Mapper(componentModel = "spring")
public interface AddressEntityMapper {
    Address mapFromEntity(AddressEntity addressEntity);
}
