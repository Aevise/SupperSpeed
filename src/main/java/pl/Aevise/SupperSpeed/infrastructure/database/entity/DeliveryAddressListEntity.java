package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DeliveryAddressKey;

@Getter
@Setter
@EqualsAndHashCode(of = {"restaurantEntity"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_address_list")
public class DeliveryAddressListEntity {

    @EmbeddedId
    private DeliveryAddressKey id;

    @MapsId("restaurantId")
    @JoinColumn(name = "restaurant_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private RestaurantEntity restaurantEntity;

    @MapsId("deliveryAddressId")
    @JoinColumn(name = "delivery_address_id")
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private DeliveryAddressEntity deliveryAddressEntity;
}
