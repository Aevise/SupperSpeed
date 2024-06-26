package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode(of = {"name", "description", "price"})
@ToString(of = {"name", "description", "price", "availability"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dish")
public class DishEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dish_id", nullable = false, unique = true)
    private Integer dishId;

    //zmienic potem na lazy i sie przekonac czy aplikacja po tym kleknie
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_category_id", nullable = false)
    private DishCategoryEntity dishCategory;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "description", length = 128)
    private String description;

    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id", unique = true)
    private ImageEntity imageEntity;

    @Column(name = "availability", nullable = false)
    private Boolean availability;

    @Column(name = "is_hidden", nullable = false)
    private Boolean isHidden;

}
