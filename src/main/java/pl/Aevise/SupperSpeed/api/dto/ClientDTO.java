package pl.Aevise.SupperSpeed.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    Integer id;
    String name;
    String surname;
    String phone;
    Integer supperUserId;
    AddressDTO addressDTO;
}
