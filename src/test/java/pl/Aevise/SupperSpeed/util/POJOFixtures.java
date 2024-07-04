package pl.Aevise.SupperSpeed.util;

import lombok.experimental.UtilityClass;
import pl.Aevise.SupperSpeed.domain.Address;
import pl.Aevise.SupperSpeed.domain.Client;
import pl.Aevise.SupperSpeed.domain.SupperUser;

import java.time.OffsetDateTime;

import static pl.Aevise.SupperSpeed.util.Constants.*;

@UtilityClass
public class POJOFixtures {
    public static Client client1() {
        return Client.builder()
                .id(1)
                .supperUser(supperUser4())
                .name("client1")
                .surname("client2")
                .address(address4())
                .phone("+12 345 678 901")
                .build();
    }

    public static SupperUser supperUser4() {
        return SupperUser.builder()
                .supperUserId(4)
                .email(TEST_CLIENT_EMAIL_1)
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .build();
    }

    public static Address address4() {
        return Address.builder()
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
