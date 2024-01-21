package pl.Aevise.SupperSpeed.domain;

import lombok.*;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "restaurantResponseId")
@ToString(of = {"description", "responseDateTime"})
public class RestaurantResponse {
    Integer restaurantResponseId;
    String description;
    OffsetDateTime responseDateTime;
}
