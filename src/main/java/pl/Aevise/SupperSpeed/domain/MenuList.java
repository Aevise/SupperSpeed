package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.DishEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.MenuEntity;

@With
@Value
@Builder
@EqualsAndHashCode(of = {"menu", "dish"})
@ToString(of = {"menu", "dish"})
public class MenuList {
    MenuEntity menu;
    DishEntity dish;
}
