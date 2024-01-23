package pl.Aevise.SupperSpeed.infrastructure.database.entity.utils;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode(of = {"dishId", "ingredientId"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class IngredientListKey implements Serializable {

    @Column(name = "dish_id")
    private Integer dishId;

    @Column(name = "ingredient_id")
    private Integer ingredientId;
}
