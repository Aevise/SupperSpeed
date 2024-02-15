package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupperOrderDTO {
    Integer orderId;
    LocalDateTime orderDateTime;
    ClientDTO clientDTO;
    RestaurantDTO restaurantDTO;
    StatusListDTO statusListDTO;
    RatingDTO ratingDTO;
}
