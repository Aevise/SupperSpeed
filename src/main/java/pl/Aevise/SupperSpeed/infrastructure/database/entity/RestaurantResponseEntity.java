package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "restaurantResponseId")
@ToString(of = {"description", "responseDateTime"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "restaurant_response")
public class RestaurantResponseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "restaurant_response_id", unique = true, nullable = false)
    private Integer restaurantResponseId;

    @Column(name = "description", length = 256, nullable = false)
    private String description;

    @Column(name = "response_date_time", nullable = false)
    private OffsetDateTime responseDateTime;
}
