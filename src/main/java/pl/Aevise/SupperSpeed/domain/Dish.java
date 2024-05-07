package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishCategoryEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

import java.math.BigDecimal;

@With
@Value
@Builder
@EqualsAndHashCode(of = "dishId")
@ToString(of = {"name", "description", "price", "availability"})
public class Dish {
    Integer dishId;
    RestaurantEntity restaurant;
    DishCategoryEntity dishCategory;
    String name;
    String description;
    BigDecimal price;
    Image image;
    Boolean availability;
    Boolean isHidden;
}
