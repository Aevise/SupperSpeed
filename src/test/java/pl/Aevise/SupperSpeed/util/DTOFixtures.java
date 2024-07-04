package pl.Aevise.SupperSpeed.util;

import lombok.experimental.UtilityClass;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.ClientDTO;

import static pl.Aevise.SupperSpeed.util.Constants.LUBLIN;
import static pl.Aevise.SupperSpeed.util.Constants.POLAND;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.client1;

@UtilityClass
public class DTOFixtures {

    public static ClientDTO clientDTO1() {
        return ClientDTO.builder()
                .id(client1().getId())
                .name("client1")
                .surname("client2")
                .phone("+48 123 567 789")
                .supperUserId(client1().getSupperUser().getSupperUserId())
                .build();
    }

    public static AddressDTO addressDTO4() {
        return AddressDTO.builder()
                .addressId(4)
                .country(POLAND)
                .city(LUBLIN)
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("4")
                .localNumber(4)
                .build();
    }
}
