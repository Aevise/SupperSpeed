package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.CuisineEntity;

import java.time.LocalTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "supperUser")
@ToString(of = {"restaurantName", "openHour", "closeHour"})
public class Restaurant {

    SupperUser supperUser;
    Integer restaurantId;
    String restaurantName;
    LocalTime openHour;
    LocalTime closeHour;
    AddressEntity address;
    String phone;
    Boolean isShown;
    CuisineEntity cuisine;
    Image image;
}
