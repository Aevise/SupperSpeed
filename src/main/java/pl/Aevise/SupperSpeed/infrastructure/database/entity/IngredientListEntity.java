package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.IngredientListKey;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = "dish")
@ToString(of = {"ingredient", "quantity", "units"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient_list")
public class IngredientListEntity implements Serializable {

    @EmbeddedId
    private IngredientListKey id;

    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private DishEntity dish;

    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private IngredientEntity ingredient;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "units", length = 16, nullable = false)
    private Integer units;

}
