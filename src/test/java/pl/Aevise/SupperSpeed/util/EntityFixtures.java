package pl.Aevise.SupperSpeed.util;

import lombok.experimental.UtilityClass;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.time.LocalTime;
import java.time.OffsetDateTime;

@UtilityClass
public class EntityFixtures {

    private final static String testPassword = "$2a$12$zjYWnQlFwPc0xP.Ls0brs.WFZ/qN/J3Z0.o/M/K7bQRW6SUTLMX42";

    public static RolesEntity rolesEntity1() {
        return RolesEntity.builder()
                .roleId(1)
                .role(AvailableRoles.CLIENT.name())
                .build();
    }

    public static RolesEntity rolesEntity2() {
        return RolesEntity.builder()
                .roleId(2)
                .role(AvailableRoles.RESTAURANT.name())
                .build();
    }

//    INSERT INTO address(address_id, country, city, postal_code, street_name, building_number)
//VALUES (1, 'Poland', 'Chelm', '22-100', 'hehe', 1);
    public static AddressEntity addressEntity1(){
        return AddressEntity.builder()
                .addressId(1)
                .country("Poland")
                .city("Warszawa")
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("1")
                .localNumber(1)
                .build();
    }
    public static AddressEntity addressEntity2(){
        return AddressEntity.builder()
                .addressId(2)
                .country("Poland")
                .city("Lublin")
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("2")
                .localNumber(2)
                .build();
    }
    public static AddressEntity addressEntity3(){
        return AddressEntity.builder()
                .addressId(3)
                .country("Poland")
                .city("Warszawa")
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("3")
                .localNumber(3)
                .build();
    }

    public static SupperUserEntity supperUserEntity1() {
        return SupperUserEntity.builder()
                .supperUserId(1)
                .email("test1@gmail.com")
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity1())
                .build();
    }

    public static SupperUserEntity supperUserEntity2() {
        return SupperUserEntity.builder()
                .supperUserId(2)
                .email("test2@gmail.com")
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity1())
                .build();
    }

    public static SupperUserEntity supperUserEntity3() {
        return SupperUserEntity.builder()
                .supperUserId(3)
                .email("test3@gmail.com")
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity1())
                .build();
    }

    public static SupperUserEntity supperUserEntity4() {
        return SupperUserEntity.builder()
                .supperUserId(4)
                .email("test4@gmail.com")
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity2())
                .build();
    }

//    INSERT INTO restaurant(user_id, restaurant_name, address_id, open_hour, close_hour, cuisine_id, is_shown)
//VALUES (1, 'test1', 1, CURRENT_TIME, CURRENT_TIME, 5, true);
    public static RestaurantEntity restaurantEntity1(){
        return RestaurantEntity.builder()
                .id(1)
                .supperUser(supperUserEntity1())
                .address(addressEntity1())
                .restaurantName("test1")
                .isShown(false)
                .phone("+48 123 456 789")
                .openHour(LocalTime.NOON)
                .closeHour(LocalTime.MIDNIGHT)
                .build();
    }
    public static RestaurantEntity restaurantEntity2(){
        return RestaurantEntity.builder()
                .id(2)
                .supperUser(supperUserEntity2())
                .address(addressEntity2())
                .restaurantName("test2")
                .isShown(false)
                .phone("+48 123 456 789")
                .openHour(LocalTime.NOON)
                .closeHour(LocalTime.MIDNIGHT)
                .build();
    }

    public static RestaurantEntity restaurantEntity3(){
        return RestaurantEntity.builder()
                .id(3)
                .supperUser(supperUserEntity3())
                .address(addressEntity3())
                .restaurantName("test3")
                .isShown(false)
                .phone("+48 123 456 789")
                .openHour(LocalTime.NOON)
                .closeHour(LocalTime.MIDNIGHT)
                .build();
    }
}
