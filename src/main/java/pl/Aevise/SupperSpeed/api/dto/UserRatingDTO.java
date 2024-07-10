package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

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

    public final Map<String, String> asMap(){
        Map<String, String> result = new HashMap<>();
        ofNullable(userRatingId).ifPresent(value -> result.put("userRatingId", userRatingId.toString()));
        ofNullable(foodRating).ifPresent(value -> result.put("foodRating", foodRating.toString()));
        ofNullable(deliveryRating).ifPresent(value -> result.put("deliveryRating", deliveryRating.toString()));
        ofNullable(ratingDateTime).ifPresent(value -> result.put("ratingDateTime", ratingDateTime));
        ofNullable(description).ifPresent(value -> result.put("description", description));
        return result;
    }
}
