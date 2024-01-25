package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    String restaurantName;
    LocalTime openHour;
    LocalTime closeHour;
    //TODO add restaurant address to DTO?
    String phone;
}
