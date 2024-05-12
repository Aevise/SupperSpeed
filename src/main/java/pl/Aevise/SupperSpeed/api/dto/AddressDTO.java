package pl.Aevise.SupperSpeed.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
