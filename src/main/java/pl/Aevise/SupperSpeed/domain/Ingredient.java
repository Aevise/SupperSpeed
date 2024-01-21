package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "ingredientId")
@ToString(of = {"name", "allergic"})
public class Ingredient {

    Integer ingredientId;
    String name;
    Boolean allergic;
}
