package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "orderId")
@ToString(of = {"client", "restaurant", "orderDateTime", "status", "userRating"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "supper_order")
public class SupperOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id", unique = true, nullable = false)
    private Integer orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    private ClientEntity client;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private RestaurantEntity restaurant;

    @Column(name = "order_date_time", nullable = false)
    private OffsetDateTime orderDateTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id", unique = true)
    private StatusListEntity status;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "rating_id", unique = true)
    private UserRatingEntity userRating;


}