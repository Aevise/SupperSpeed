package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpinionDTO {

    Integer orderId;
    String orderDateTime;
    Map<DishDTO, Integer> dishesAndQuantity;

    ClientDTO clientDTO;
    RestaurantDTO restaurantDTO;
    UserRatingDTO userRatingDTO;
}
