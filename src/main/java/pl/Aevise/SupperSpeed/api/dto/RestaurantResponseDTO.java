package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantResponseDTO {

    Integer restaurantResponseId;
    String description;
    String responseDateTime;

    public final Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        ofNullable(restaurantResponseId).ifPresent(value -> result.put("restaurantResponseId", restaurantResponseId.toString()));
        ofNullable(description).ifPresent(value -> result.put("description", description));
        ofNullable(responseDateTime).ifPresent(value -> result.put("responseDateTime", responseDateTime));
        return result;
    }
}
