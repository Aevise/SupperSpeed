package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "addressId")
@ToString(of = {"addressId", "country", "city", "postalCode", "currentAddress"})
public class Address {
    Integer addressId;
    String country;
    String city;
    String postalCode;
    String currentAddress;
}
