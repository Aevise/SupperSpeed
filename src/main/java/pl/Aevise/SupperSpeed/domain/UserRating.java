package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "userRatingId")
@ToString(of = {"foodRating", "deliveryRating", "ratingDateTime", "description"})
public class UserRating {
    Integer userRatingId;
    Integer foodRating;
    Integer deliveryRating;
    OffsetDateTime ratingDateTime;
    String description;
    RestaurantResponseEntity restaurantResponse;
}
