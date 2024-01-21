package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.ClientEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.StatusListEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "orderId")
@ToString(of = {"client", "restaurant", "orderDateTime", "status", "userRating"})
public class SupperOrder {
    Integer orderId;
    ClientEntity client;
    RestaurantEntity restaurant;
    OffsetDateTime orderDateTime;
    StatusListEntity status;
    UserRatingEntity userRating;
}
