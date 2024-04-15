package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "supperOrder")
@ToString(of = {"dish", "quantity"})
public class DishList {

    Dish dish;
    SupperOrder supperOrder;
    Integer quantity;
}
