package pl.Aevise.SupperSpeed.domain;

import lombok.*;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "orderId")
@ToString(of = {"orderDateTime", "status", "userRating"})
public class SupperOrder {
    Integer orderId;
    Client client;
    Restaurant restaurant;
    OffsetDateTime orderDateTime;
    StatusList status;
    UserRating userRating;
}
