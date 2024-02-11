package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = "dishId")
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private RestaurantEntity restaurant;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "dish_category_id", nullable = false)
    private DishCategoryEntity dishCategory;

    @Column(name = "name", length = 32, nullable = false)
    private String name;

    @Column(name = "description", length = 128)
    private String description;

    @Column(name = "price", nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @Column(name = "photo", length = 128)
    private String photo;

    @Column(name = "availability", nullable = false)
    private Boolean availability;

    @ManyToMany
    @JoinTable(
            name = "menu_list",
            joinColumns = @JoinColumn(name = "dish_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_id")
    )
    private Set<MenuEntity> menus;

}
