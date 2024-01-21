package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.IngredientEntity;

@With
@Value
@Builder
@EqualsAndHashCode(of = "dish")
@ToString(of = {"ingredient", "quantity", "units"})
public class IngredientList {
    DishEntity dish;
    IngredientEntity ingredient;
    Integer quantity;
    Integer units;
}
