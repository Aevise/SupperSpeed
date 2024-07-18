package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface AddressEntityMapper {
    Address mapFromEntity(AddressEntity addressEntity);
}
