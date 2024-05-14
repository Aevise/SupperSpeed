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

    Integer userId;
    Integer restaurantId;
    String restaurantName;
    LocalTime openHour;
    LocalTime closeHour;
    String phone;
    Boolean isShown;
    CuisineDTO cuisine;
    ImageDTO imageDTO;
    AddressDTO address;

}
