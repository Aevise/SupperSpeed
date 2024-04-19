package pl.Aevise.SupperSpeed.domain;

import lombok.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;

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

}
