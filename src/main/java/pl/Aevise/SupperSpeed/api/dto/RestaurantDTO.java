package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.Aevise.SupperSpeed.domain.SupperUser;
import pl.Aevise.SupperSpeed.infrastructure.security.dto.SupperUserDTO;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {

    SupperUserDTO supperUser;
    Integer restaurantId;
    String restaurantName;
    LocalTime openHour;
    LocalTime closeHour;
    String phone;
    CuisineDTO cuisine;
    LogoDTO logo;
    AddressDTO address;
    //TODO dodac address do mapowania restauracji

}
