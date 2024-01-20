package pl.Aevise.SupperSpeed.infrastructure.database.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@EqualsAndHashCode(of = "supperUser")
@ToString(of = {"restaurantName", "openHour", "closeHour"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant")
public class RestaurantEntity {

    @Id
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    private SupperUserEntity supperUser;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "open_hour")
    private LocalTime openHour;

    @Column(name = "close_hour")
    private LocalTime closeHour;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private AddressEntity address;

}
