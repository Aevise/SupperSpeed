package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.SupperUserEntity;

import java.time.LocalTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "supperUser")
@ToString(of = {"restaurantName", "openHour", "closeHour"})
public class Restaurant {

    SupperUserEntity supperUser;
    String restaurantName;
    LocalTime openHour;
    LocalTime closeHour;
    AddressEntity address;
    String phone;
}
