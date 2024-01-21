package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;

@With
@Value
@Builder
@EqualsAndHashCode(of = "dishCategoryId")
@ToString(of = {"categoryName"})
public class DishCategory {
    Integer dishCategoryId;
    String categoryName;
    RestaurantEntity restaurant;
}
