package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "addressId")
@ToString(of = {"addressId", "country", "city", "postalCode", "streetName"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "address")
public class AddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false, unique = true)
    private Integer addressId;

    @Column(name = "country", length = 32, nullable = false)
    private String country;

    @Column(name = "city", length = 32, nullable = false)
    private String city;

    @Column(name = "postal_code", length = 32, nullable = false)
    private String postalCode;

    @Column(name = "street_name", length = 64, nullable = false)
    private String streetName;

    @Column(name = "building_number", length = 12)
    private String buildingNumber;

    @Column(name = "local_number")
    private Integer localNumber;
}