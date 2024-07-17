package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestOpinionDTO {

    String orderDateTime;
    String restaurantName;
    String clientName;
    String clientSurname;
    UserRatingDTO userRating;

}
