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
    String phone;
    CuisineDTO cuisine;
    LogoDTO logo;
    AddressDTO addressDTO;
    //TODO dodac address do mapowania restauracji

}
