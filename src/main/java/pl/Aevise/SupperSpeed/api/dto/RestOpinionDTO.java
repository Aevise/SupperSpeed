package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
