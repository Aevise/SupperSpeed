package pl.Aevise.SupperSpeed.infrastructure.database.repository.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.Aevise.SupperSpeed.domain.Image;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ImageEntity;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageEntityMapper {

    Image mapFromEntity(ImageEntity imageEntity);

    ImageEntity mapToEntity(Image image);
}
