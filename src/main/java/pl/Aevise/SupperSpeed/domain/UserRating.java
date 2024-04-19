package pl.Aevise.SupperSpeed.domain;

import lombok.*;

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
    RestaurantResponse restaurantResponse;
}
