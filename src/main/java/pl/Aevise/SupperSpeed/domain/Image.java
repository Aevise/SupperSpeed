package pl.Aevise.SupperSpeed.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import lombok.With;

@With
@Value
@Builder
@EqualsAndHashCode(of = "imageId")
public class Image {
    Integer imageId;
    String imageURL;
}
