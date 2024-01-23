package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "addressId")
@ToString(of = {"addressId", "country", "city", "postalCode", "address"})
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

    @Column(name = "address", length = 64, nullable = false)
    private String address;
}