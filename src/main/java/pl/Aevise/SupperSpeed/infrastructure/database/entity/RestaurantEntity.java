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
    @Column(name = "restaurant_id", unique = true, nullable = false)
    private Integer id;

//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "restaurant_id")
//    private Integer restaurantId;

    @MapsId
    @JoinColumn(name = "supper_restaurant_id")
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private SupperUserEntity supperUser;

    @Column(name = "restaurant_name")
    private String restaurantName;

    @Column(name = "open_hour")
    private LocalTime openHour;

    @Column(name = "is_shown")
    private boolean isShown;

    @Column(name = "close_hour")
    private LocalTime closeHour;

    @Column(name = "phone")
    private String phone;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", unique = true)
    private AddressEntity address;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinColumn(name = "cuisine_id")
    private CuisineEntity cuisine;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", unique = true)
    private ImageEntity imageEntity;

    //TODO czy tutaj dodac kiedys Cascade? Do sprawdzenia na testach
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    private Set<SupperOrderEntity> orders;
}
