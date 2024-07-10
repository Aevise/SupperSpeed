package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = {"city", "country", "postalCode", "streetName"})
@ToString(of = {"country", "city", "district"})
public class DeliveryAddress {

    Integer deliveryAddressId;
    String country;
    String city;
    String district;
    String postalCode;
    String streetName;
}
