package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode(of = "dishCategoryId")
@ToString(of = {"categoryName"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dish_category")
public class DishCategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_category_id", nullable = false, unique = true)
    private Integer dishCategoryId;

    @Column(name = "category_name", length = 32, nullable = false)
    private String categoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId
    private RestaurantEntity restaurant;

}
