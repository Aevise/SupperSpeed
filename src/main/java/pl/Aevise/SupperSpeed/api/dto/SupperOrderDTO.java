package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupperOrderDTO {
    Integer orderId;
    String orderDateTime;
    ClientDTO clientDTO;
    RestaurantDTO restaurantDTO;
    StatusListDTO statusListDTO;
    RatingDTO ratingDTO;
}
