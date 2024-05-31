package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "district")
@ToString(of = {"country", "city", "district"})
public class DeliveryAddress {

    Integer deliveryAddressId;
    String country;
    String city;
    String district;
    String cuisine;
    String postalCode;
    String streetName;
}
