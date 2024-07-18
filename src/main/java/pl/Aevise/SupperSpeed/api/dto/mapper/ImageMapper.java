package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import pl.Aevise.SupperSpeed.api.dto.ImageDTO;
import pl.Aevise.SupperSpeed.domain.Image;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface ImageMapper {

    ImageDTO mapToDTO(final Image image);

    Image mapFromDTO(final ImageDTO logo);
}
