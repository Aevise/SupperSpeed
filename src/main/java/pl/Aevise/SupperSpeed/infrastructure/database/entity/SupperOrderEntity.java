package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "orderId")
@ToString(of = {"client", "restaurant", "orderDateTime", "status", "userRating"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supper_order")
public class SupperOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false)
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
// sprawdzic    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
// sprawdzic    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private RestaurantEntity restaurant;

    @Column(name = "order_date_time", nullable = false)
    private OffsetDateTime orderDateTime;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "status_id", unique = true)
    private StatusListEntity status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id", unique = true)
    private UserRatingEntity userRating;



}