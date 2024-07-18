package pl.Aevise.SupperSpeed.api.dto.mapper;

import org.mapstruct.AnnotateWith;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import pl.Aevise.SupperSpeed.infrastructure.util.GeneratedMapper;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@AnnotateWith(GeneratedMapper.class)
@Mapper(componentModel = "spring")
public interface OffsetDateTimeMapper {

    DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneOffset.UTC);
    DateTimeFormatter DATE_FORMAT_FOR_IMAGES = DateTimeFormatter.ofPattern("yyyy_MM_dd").withZone(ZoneOffset.UTC);

    @Named("mapOffsetDateTimeToString")
    default String mapOffsetDateTimeToString(OffsetDateTime offsetDateTime) {
        return Optional.ofNullable(offsetDateTime)
                .map(odt -> odt.atZoneSameInstant(ZoneOffset.UTC))
                .map(odt -> odt.format(DATE_FORMAT))
                .orElse(null);
    }

    @Named("mapOffsetDateTimeToStringForImages")
    default String mapOffsetDateTimeToStringForImages(OffsetDateTime offsetDateTime) {
        return Optional.ofNullable(offsetDateTime)
                .map(odt -> odt.atZoneSameInstant(ZoneOffset.UTC))
                .map(odt -> odt.format(DATE_FORMAT_FOR_IMAGES))
                .orElse(null);
    }

    @Named("mapStringToOffsetDateTime")
    default OffsetDateTime mapStringToOffsetDateTime(String date) {
        return Optional.ofNullable(date)
                .map(d -> OffsetDateTime.parse(d, DATE_FORMAT))
                .orElse(null);
    }
}
