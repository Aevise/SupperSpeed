package pl.Aevise.SupperSpeed.infrastructure.database.entity.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"orderId"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class DeliveryAddressKey implements Serializable {

    @Column(name = "restaurant_id")
    private Integer restaurantId;

    @Column(name = "delivery_address_id")
    private Integer deliveryAddressId;
}
