package pl.Aevise.SupperSpeed.domain;

import lombok.*;

import java.time.OffsetDateTime;

@With
@Value
@Builder
@EqualsAndHashCode(of = "supperUserId")
@ToString(of = {"email", "active", "creationDateTime", "lastLoginDateTime"})
public class SupperUser {

    Integer supperUserId;
    String email;
    String password;
    Boolean active;
    OffsetDateTime creationDateTime;
    OffsetDateTime lastLoginDateTime;

}
