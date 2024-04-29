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
public class UserRatingDTO {

    Integer userRatingId;
    Integer foodRating;
    Integer deliveryRating;
    String ratingDateTime;
    String description;
    RestaurantResponseDTO restaurantResponseDTO;
}
