package pl.Aevise.SupperSpeed.domain;

import lombok.*;

@With
@Value
@Builder
@EqualsAndHashCode(of = "supperUser")
@ToString(of = {"name", "surname"})
public class Client {
    Integer id;
    SupperUser supperUser;
    String name;
    String surname;
    Address address;
    String phone;
    Boolean isShown;
}
