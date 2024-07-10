package pl.Aevise.SupperSpeed.util;

import lombok.experimental.UtilityClass;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.utils.DishesListKey;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.RolesEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.database.entity.SupperUserEntity;
import pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.OffsetDateTime;

import static pl.Aevise.SupperSpeed.util.Constants.*;

@UtilityClass
public class EntityFixtures {

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

    public static AddressEntity addressEntity1() {
        return AddressEntity.builder()
                .country(POLAND)
                .city(WARSZAWA)
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("1")
                .localNumber(1)
                .build();
    }

    public static AddressEntity addressEntity2() {
        return AddressEntity.builder()
                .country(POLAND)
                .city(WARSZAWA)
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("2")
                .localNumber(2)
                .build();
    }

    public static AddressEntity addressEntity3() {
        return AddressEntity.builder()
                .country(POLAND)
                .city(LUBLIN)
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("3")
                .localNumber(3)
                .build();
    }

    public static AddressEntity addressEntity4() {
        return AddressEntity.builder()
                .country(POLAND)
                .city(LUBLIN)
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("4")
                .localNumber(4)
                .build();
    }

    public static AddressEntity addressEntity5() {
        return AddressEntity.builder()
                .country(POLAND)
                .city(LUBLIN)
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("5")
                .localNumber(5)
                .build();
    }

    public static SupperUserEntity supperUserEntity1() {
        return SupperUserEntity.builder()
                .email("test1@gmail.com")
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity2())
                .build();
    }

    public static SupperUserEntity supperUserEntity2() {
        return SupperUserEntity.builder()
                .email("test2@gmail.com")
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity2())
                .build();
    }

    public static SupperUserEntity supperUserEntity3() {
        return SupperUserEntity.builder()
                .email("test3@gmail.com")
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity2())
                .build();
    }

    public static SupperUserEntity supperUserEntity4() {
        return SupperUserEntity.builder()
                .email(TEST_CLIENT_EMAIL_1)
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity1())
                .build();
    }

    public static SupperUserEntity supperUserEntity5() {
        return SupperUserEntity.builder()
                .email("test5@gmail.com")
                .password(testPassword)
                .active(true)
                .creationDateTime(OffsetDateTime.now())
                .lastLoginDateTime(OffsetDateTime.now())
                .role(rolesEntity1())
                .build();
    }

    public static RestaurantEntity restaurantEntity1() {
        return RestaurantEntity.builder()
                .supperUser(supperUserEntity1())
                .address(addressEntity1())
                .restaurantName("restaurant_test_1")
                .isShown(false)
                .phone("+48 123 456 789")
                .openHour(LocalTime.MIDNIGHT)
                .closeHour(LocalTime.NOON)
                .build();
    }

    public static RestaurantEntity restaurantEntity2() {
        return RestaurantEntity.builder()
                .supperUser(supperUserEntity2())
                .address(addressEntity2())
                .restaurantName("restaurant_test_2")
                .isShown(false)
                .phone("+48 123 456 789")
                .openHour(LocalTime.NOON)
                .closeHour(LocalTime.MIDNIGHT)
                .build();
    }

    public static RestaurantEntity restaurantEntity3() {
        return RestaurantEntity.builder()
                .supperUser(supperUserEntity3())
                .address(addressEntity3())
                .restaurantName("restaurant_test_3")
                .isShown(false)
                .phone("+48 123 456 789")
                .openHour(LocalTime.NOON)
                .closeHour(LocalTime.MIDNIGHT)
                .build();
    }

    public static ClientEntity clientEntity1() {
        return ClientEntity.builder()
                .supperUser(supperUserEntity4())
                .address(addressEntity4())
                .name("test1")
                .surname("client1")
                .phone("+12 345 678 901")
                .build();
    }

    public static ClientEntity clientEntity2() {
        return ClientEntity.builder()
                .supperUser(supperUserEntity5())
                .address(addressEntity5())
                .name("test2")
                .surname("client2")
                .phone("+12 345 678 901")
                .build();
    }

    public static CuisineEntity cuisineEntity1() {
        return CuisineEntity.builder()
                .cuisine(CUISINES.get("Italian"))
                .build();
    }

    public static CuisineEntity cuisineEntity2() {
        return CuisineEntity.builder()
                .cuisine(CUISINES.get("Polish"))
                .build();
    }

    public static DeliveryAddressEntity deliveryAddressEntity1() {
        return DeliveryAddressEntity.builder()
                .country(POLAND)
                .city(WARSZAWA)
                .district("district1")
                .postalCode("11-111")
                .streetName("street1")
                .build();
    }

    public static DeliveryAddressEntity deliveryAddressEntity2() {
        return DeliveryAddressEntity.builder()
                .country(POLAND)
                .city(LUBLIN)
                .district("district2")
                .postalCode("22-222")
                .streetName("street2")
                .build();
    }

    public static DeliveryAddressEntity deliveryAddressEntity3() {
        return DeliveryAddressEntity.builder()
                .country(POLAND)
                .city(LUBLIN)
                .district("district3")
                .postalCode("22-222")
                .streetName("street3")
                .build();
    }

    public static DishEntity dishEntity1() {
        return DishEntity.builder()
                .name("dish_test1")
                .price(BigDecimal.ONE)
                .availability(true)
                .isHidden(false)
                .build();
    }

    public static DishEntity dishEntity2() {
        return DishEntity.builder()
                .name("dish_test2")
                .price(BigDecimal.ONE)
                .availability(true)
                .isHidden(false)
                .build();
    }

    public static DishEntity dishEntity3() {
        return DishEntity.builder()
                .name("dish_test3")
                .price(BigDecimal.ONE)
                .availability(true)
                .isHidden(false)
                .build();
    }

    public static SupperOrderEntity buildSupperOrderEntity(RestaurantEntity restaurant, ClientEntity clientEntity) {
        return SupperOrderEntity.builder()
                .restaurant(restaurant)
                .client(clientEntity)
                .orderDateTime(OffsetDateTime.now())
                .status(StatusListEntity.builder()
                        .statusId(1)
                        .build())
                .build();
    }

    public static DishesListEntity buildDishesListEntity(DishEntity dish, SupperOrderEntity order) {
        return DishesListEntity.builder()
                .id(DishesListKey.builder()
                        .dishId(dish.getDishId())
                        .orderId(order.getOrderId())
                        .build())
                .dish(dish)
                .order(order)
                .quantity(100)
                .build();
    }

    public static UserRatingEntity buildUserRating1() {
        return UserRatingEntity.builder()
                .foodRating(1)
                .deliveryRating(1)
                .ratingDateTime(OffsetDateTime.now())
                .description("test1")
                .build();
    }

    public static UserRatingEntity buildUserRating2() {
        return UserRatingEntity.builder()
                .foodRating(2)
                .deliveryRating(2)
                .ratingDateTime(OffsetDateTime.now())
                .description("test2")
                .build();
    }
}
