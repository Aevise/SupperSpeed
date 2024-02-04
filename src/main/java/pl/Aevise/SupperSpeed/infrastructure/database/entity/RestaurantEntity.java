package pl.Aevise.SupperSpeed.infrastructure.database.entity;


import jakarta.persistence.*;
import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;

import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "supperUser")
@ToString(of = {"restaurantName", "cuisine", "openHour", "closeHour"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant")
public class RestaurantEntity {

    @Id
    private Integer id;

    @MapsId
    @JoinColumn(name = "supper_restaurant_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SupperUserEntity supperUser;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "open_hour")
    private LocalTime openHour;

    @Column(name = "close_hour")
    private LocalTime closeHour;

    @Column(name = "phone")
    private String phone;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private AddressEntity address;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cuisine_id")
    private CuisineEntity cuisine;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "logo_id", unique = true)
    private LogoEntity logo;

    //TODO czy tutaj dodac kiedys Cascade? Do sprawdzenia na testach
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private Set<SupperOrderEntity> orders;

}
