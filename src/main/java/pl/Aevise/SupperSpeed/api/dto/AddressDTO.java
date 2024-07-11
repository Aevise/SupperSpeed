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
public class AddressDTO {

    Integer addressId;
    String country;
    String city;
    String postalCode;
    String streetName;
    String buildingNumber;
    Integer localNumber;

    public Map<String, String> asMap(){
        Map<String, String> result = new HashMap<>();
        ofNullable(addressId).ifPresent(value ->result.put("addressId", addressId.toString()));
        ofNullable(country).ifPresent(value ->result.put("country", country));
        ofNullable(city).ifPresent(value ->result.put("city", city));
        ofNullable(postalCode).ifPresent(value ->result.put("postalCode", postalCode));
        ofNullable(streetName).ifPresent(value ->result.put("streetName", streetName));
        ofNullable(buildingNumber).ifPresent(value ->result.put("buildingNumber", buildingNumber));
        ofNullable(localNumber).ifPresent(value ->result.put("localNumber", localNumber.toString()));
        return result;
    }
}
