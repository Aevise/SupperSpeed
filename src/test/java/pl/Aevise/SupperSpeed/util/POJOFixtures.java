package pl.Aevise.SupperSpeed.util;

import lombok.experimental.UtilityClass;
import pl.Aevise.SupperSpeed.domain.*;

import java.math.BigDecimal;
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

    public static Restaurant restaurant1() {
        return Restaurant.builder()
                .restaurantId(1)
                .build();
    }

    public static Address address1() {
        return Address.builder()
                .addressId(1)
                .country(POLAND)
                .city(WARSZAWA)
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("1")
                .localNumber(1)
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

    public static DeliveryAddressList deliveryAddressList1() {
        return DeliveryAddressList.builder()
                .restaurantId(restaurant1().getRestaurantId())
                .deliveryAddress(deliveryAddress1())
                .build();
    }

    public static DeliveryAddressList deliveryAddressList2() {
        return DeliveryAddressList.builder()
                .restaurantId(restaurant1().getRestaurantId())
                .deliveryAddress(deliveryAddress2())
                .build();
    }

    public static DeliveryAddress deliveryAddress1() {
        return DeliveryAddress.builder()
                .deliveryAddressId(1)
                .country(POLAND)
                .city(WARSZAWA)
                .district("Targowek")
                .postalCode("12-345")
                .streetName("Test1")
                .build();
    }

    public static DeliveryAddress deliveryAddress2() {
        return DeliveryAddress.builder()
                .deliveryAddressId(2)
                .country(POLAND)
                .city(WARSZAWA)
                .district("Targowek")
                .postalCode("12-345")
                .streetName("Test2")
                .build();
    }

    public static SupperOrder supperOrder1() {
        return SupperOrder.builder()
                .orderId(1)
                .client(client1())
                .restaurant(restaurant1())
                .orderDateTime(OffsetDateTime.now())
                .build();
    }

    public static StatusList statusList1() {
        return StatusList.builder()
                .statusId(1)
                .description("NEW")
                .build();
    }

    public static Dish dish1() {
        return Dish.builder()
                .dishId(1)
                .name("dish 1")
                .description("I am dish 1")
                .price(BigDecimal.TEN)
                .availability(true)
                .isHidden(false)
                .build();
    }

    public static DishCategory dishCategory1() {
        return DishCategory.builder()
                .dishCategoryId(1)
                .categoryName("dish category 1")
                .build();
    }
}