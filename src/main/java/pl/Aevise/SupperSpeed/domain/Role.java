package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "roleId")
@ToString(of = "roleName")
public class Role {
    Integer roleId;
    String roleName;
}
