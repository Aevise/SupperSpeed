package pl.Aevise.SupperSpeed.infrastructure.security.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SupperUserDTO {

    Integer supperUserId;
    @Email(message = "Please provide correct correct email ex.: XXXX@XXX.XX")
    @NotEmpty(message = "Email field must not be empty!")
    String email;
    Boolean active;
    OffsetDateTime creationDateTime;
    OffsetDateTime lastLoginDateTime;

    public final Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        ofNullable(supperUserId).ifPresent(value -> result.put("supperUserId", supperUserId.toString()));
        ofNullable(email).ifPresent(value -> result.put("email", email));
        ofNullable(active).ifPresent(value -> result.put("active", active.toString()));
        ofNullable(creationDateTime).ifPresent(value -> result.put("creationDateTime", creationDateTime.toString()));
        ofNullable(lastLoginDateTime).ifPresent(value -> result.put("lastLoginDateTime", lastLoginDateTime.toString()));
        return result;
    }
}
