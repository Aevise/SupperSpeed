package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "cuisineId")
@ToString(of = "cuisine")
public class Cuisine {
    Integer cuisineId;
    String cuisine;
}
