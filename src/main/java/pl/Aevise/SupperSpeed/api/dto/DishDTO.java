package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
}
