package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"city", "country", "postalCode"})
@ToString(of = {"country", "city", "district"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "delivery_address")
public class DeliveryAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delivery_address_id", nullable = false, unique = true)
    private Integer deliveryAddressId;

    @Column(name = "country", length = 32, nullable = false)
    private String country;

    @Column(name = "city", length = 64, nullable = false)
    private String city;

    @Column(name = "district", length = 32, nullable = false)
    private String district;

    @Column(name = "postal_code", length = 64, nullable = false)
    private String postalCode;

    @Column(name = "street_name", length = 64, nullable = false)
    private String streetName;

}
