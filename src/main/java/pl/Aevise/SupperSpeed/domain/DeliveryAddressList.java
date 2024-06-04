package pl.Aevise.SupperSpeed.domain;


import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "restaurantId")
@ToString(of = {"deliveryAddress", "restaurantId"})
public class DeliveryAddressList {

    DeliveryAddress deliveryAddress;
    Integer restaurantId;
}
