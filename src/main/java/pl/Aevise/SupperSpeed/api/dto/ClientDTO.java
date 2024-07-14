package pl.Aevise.SupperSpeed.api.dto;


import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import static java.util.Optional.ofNullable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    Integer id;
    String name;
    String surname;
    @Size
    @Pattern(regexp = "^[+]\\d{2}\\s\\d{3}\\s\\d{3}\\s\\d{3}$")
    String phone;
    Integer supperUserId;
    AddressDTO addressDTO;

    public final Map<String, String> asMap() {
        Map<String, String> result = new HashMap<>();
        ofNullable(id).ifPresent(value -> result.put("id", value.toString()));
        ofNullable(name).ifPresent(value -> result.put("name", name));
        ofNullable(surname).ifPresent(value -> result.put("surname", surname));
        ofNullable(phone).ifPresent(value -> result.put("phone", phone));
        ofNullable(supperUserId).ifPresent(value -> result.put("supperUserId", supperUserId.toString()));
        return result;
    }
}
