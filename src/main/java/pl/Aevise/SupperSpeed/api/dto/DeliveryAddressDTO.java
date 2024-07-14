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
public class DeliveryAddressDTO {

    Integer deliveryAddressId;
    String country;
    String city;
    String district;
    String postalCode;
    String streetName;

    public final Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        ofNullable(deliveryAddressId).ifPresent(value -> result.put("deliveryAddressId", deliveryAddressId.toString()));
        ofNullable(country).ifPresent(value -> result.put("country", country));
        ofNullable(city).ifPresent(value -> result.put("city", city));
        ofNullable(district).ifPresent(value -> result.put("district", district));
        ofNullable(postalCode).ifPresent(value -> result.put("postalCode", postalCode));
        ofNullable(streetName).ifPresent(value -> result.put("streetName", streetName));
        return result;
    }
}
