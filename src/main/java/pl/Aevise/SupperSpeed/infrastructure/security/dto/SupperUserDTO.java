package pl.Aevise.SupperSpeed.infrastructure.security.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupperUserDTO {

    Integer supperUserId;
    String email;
    Boolean active;
    OffsetDateTime creationDateTime;
    OffsetDateTime lastLoginDateTime;
}
