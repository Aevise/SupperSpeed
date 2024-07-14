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
public class DishCategoryDTO {

    Integer dishCategoryId;
    String categoryName;

    public final Map<String, String> asMap(){
        Map<String, String> result = new HashMap<>();
        ofNullable(dishCategoryId).ifPresent(value -> result.put("dishCategoryId", dishCategoryId.toString()));
        ofNullable(categoryName).ifPresent(value -> result.put("categoryName", categoryName));
        return result;
    }
}
