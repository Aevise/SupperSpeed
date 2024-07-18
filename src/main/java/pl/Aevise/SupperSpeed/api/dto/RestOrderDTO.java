package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestOrderDTO {

    Integer orderId;
    String orderDateTime;
    String restaurantName;
    String status;
    BigDecimal totalPrice;
    List<Map<Integer, DishDTO>> dishesAndQuantity;
    UserRatingDTO userRating;
}
