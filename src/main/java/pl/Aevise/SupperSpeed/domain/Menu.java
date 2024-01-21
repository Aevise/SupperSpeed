package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "menuId")
@ToString(of = {"name", "description"})
public class Menu {
    Integer menuId;
    String name;
    String description;
}
