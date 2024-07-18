package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.*;
import pl.Aevise.SupperSpeed.api.dto.OpinionDTO;
import pl.Aevise.SupperSpeed.api.dto.RestOpinionDTO;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface OpinionMapper {

    @Mapping(source = "restaurantDTO.restaurantName", target = "restaurantName")
    @Mapping(source = "clientDTO.name", target = "clientName")
    @Mapping(source = "clientDTO.surname", target = "clientSurname")
    @Mapping(source = "userRatingDTO", target = "userRating")
    @BeanMapping(builder = @Builder(disableBuilder = true))
    RestOpinionDTO mapToRestDTO(final OpinionDTO opinionDTO);

    @AfterMapping
    default void setNullUserId(@MappingTarget RestOpinionDTO restOpinionDTO) {
        if (restOpinionDTO.getUserRating() != null) {
            restOpinionDTO.getUserRating().setUserRatingId(null);
        }
    }

}
