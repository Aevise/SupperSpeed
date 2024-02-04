package pl.Aevise.SupperSpeed.api.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTO {

    String name;
    String surname;
    String phone;

}
