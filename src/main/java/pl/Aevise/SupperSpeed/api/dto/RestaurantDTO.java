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

    //    SupperUserDTO supperUser;
    Integer restaurantId;
    String restaurantName;
    LocalTime openHour;
    LocalTime closeHour;
    String phone;
    boolean isShown;
    CuisineDTO cuisine;
    ImageDTO imageDTO;
    AddressDTO address;
    //TODO dodac address do mapowania restauracji

}
