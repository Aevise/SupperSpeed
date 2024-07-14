package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    Integer dishId;
    String name;
    String description;
    BigDecimal price;
    ImageDTO imageDTO;
    Boolean availability;
    Boolean isHidden;

    public final Map<String, String> asMap(){
        Map<String, String> result = new HashMap<>();
        ofNullable(dishId).ifPresent(value -> result.put("dishId", dishId.toString()));
        ofNullable(name).ifPresent(value -> result.put("name", name));
        ofNullable(description).ifPresent(value -> result.put("description", description));
        ofNullable(price).ifPresent(value -> result.put("price", price.toString()));
        ofNullable(availability).ifPresent(value -> result.put("availability", availability.toString()));
        ofNullable(isHidden).ifPresent(value -> result.put("isHidden", isHidden.toString()));
        return result;
    }
}
