package pl.Aevise.SupperSpeed.infrastructure.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = "dish")
@ToString(of = {"ingredientId", "quantity", "units"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ingredient_list")
public class IngredientListEntity implements Serializable{

    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dish_id", referencedColumnName = "dish_id")
    private DishEntity dish;

    @Id
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "ingredient_id")
    private IngredientEntity ingredient;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "units", length = 16, nullable = false)
    private Integer units;

}
