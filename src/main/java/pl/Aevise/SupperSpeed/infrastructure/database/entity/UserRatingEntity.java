package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "userRatingId")
@ToString(of = {"foodRating", "deliveryRating", "ratingDateTime", "description"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_rating")
public class UserRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_rating_id", nullable = false, unique = true)
    private Integer userRatingId;

    @Column(name = "food_rating", nullable = false)
    private Integer foodRating;

    @Column(name = "delivery_rating", nullable = false)
    private Integer deliveryRating;

    @Column(name = "rating_date_time", nullable = false)
    private OffsetDateTime ratingDateTime;

    @Column(name = "description", length = 256)
    private String description;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "restaurant_response_id", unique = true)
    private RestaurantResponseEntity restaurantResponseEntity;

}
