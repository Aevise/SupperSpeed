package pl.Aevise.SupperSpeed.api.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

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
    @Size
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    String phone;
    Boolean isShown;
    CuisineDTO cuisine;
    ImageDTO imageDTO;
    AddressDTO address;

    public final Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        ofNullable(userId).ifPresent(value -> result.put("userId", userId.toString()));
        ofNullable(restaurantId).ifPresent(value -> result.put("restaurantId", restaurantId.toString()));
        ofNullable(restaurantName).ifPresent(value -> result.put("restaurantName", restaurantName));
        ofNullable(openHour).ifPresent(value -> result.put("openHour", openHour.toString()));
        ofNullable(closeHour).ifPresent(value -> result.put("closeHour", closeHour.toString()));
        ofNullable(phone).ifPresent(value -> result.put("phone", phone));
        ofNullable(isShown).ifPresent(value -> result.put("isShown", isShown.toString()));
        return result;
    }
}
