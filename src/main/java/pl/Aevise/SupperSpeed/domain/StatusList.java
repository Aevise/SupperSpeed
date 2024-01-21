package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "statusId")
@ToString(of = "description")
public class StatusList {
    Integer statusId;
    String description;
}
