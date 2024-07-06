package pl.Aevise.SupperSpeed.util;

import lombok.experimental.UtilityClass;
import pl.Aevise.SupperSpeed.api.dto.*;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.AddressEntity;

import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.TimeZone;

import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.EntityFixtures.*;
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

    public static RestaurantDTO restaurantDTO1() {
        return RestaurantDTO.builder()
                .userId(1)
                .restaurantId(1)
                .restaurantName("restaurant_test_1")
                .openHour(LocalTime.NOON)
                .closeHour(LocalTime.MIDNIGHT)
                .phone("+48 123 456 789")
                .address(addressDTO1())
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

    public static AddressDTO addressDTO1() {
        return AddressDTO.builder()
                .country(POLAND)
                .city(WARSZAWA)
                .postalCode("11-222")
                .streetName("Jaskrawa")
                .buildingNumber("1")
                .localNumber(1)
                .build();
    }

    public static DeliveryAddressDTO deliveryAddressDTO1(){
        return DeliveryAddressDTO.builder()
                .deliveryAddressId(1)
                .country(POLAND)
                .city(WARSZAWA)
                .district("Targowek")
                .postalCode("12-345")
                .streetName("Test1")
                .build();
    }

    public static DeliveryAddressDTO deliveryAddressDTO2(){
        return DeliveryAddressDTO.builder()
                .deliveryAddressId(2)
                .country(POLAND)
                .city(WARSZAWA)
                .district("Targowek")
                .postalCode("12-345")
                .streetName("Test2")
                .build();
    }
    public static DeliveryAddressDTO deliveryAddressDTO3(){
        return DeliveryAddressDTO.builder()
                .deliveryAddressId(3)
                .country(POLAND)
                .city(WARSZAWA)
                .district("Targowek")
                .postalCode("12-345")
                .streetName("Test3")
                .build();
    }
    public static DeliveryAddressDTO deliveryAddressDTO4(){
        return DeliveryAddressDTO.builder()
                .deliveryAddressId(4)
                .country(POLAND)
                .city(WARSZAWA)
                .district("Targowek")
                .postalCode("12-345")
                .streetName("Test4")
                .build();
    }

    public static CuisineDTO cuisineDTO1(){
        return CuisineDTO.builder()
                .cuisine(CUISINES.get("Italian"))
                .build();
    }
    public static CuisineDTO cuisineDTO2(){
        return CuisineDTO.builder()
                .cuisine(CUISINES.get("Polish"))
                .build();
    }

    public static OpinionDTO opinionDTO1(){
        return OpinionDTO.builder()
                .orderId(1)
                .orderDateTime(OffsetDateTime.now().toString())
                .clientDTO(clientDTO1())
                .restaurantDTO(restaurantDTO1())
                .build();
    }

    public static TotalRestaurantRatingDTO totalRestaurantRatingDTO1(){
        return TotalRestaurantRatingDTO.builder()
                .restaurantId(restaurantDTO1().getRestaurantId())
                .amountOfRatedOrders(1)
                .foodRating(2.0)
                .deliveryRating(3.0)
                .build();
    }

    public static DishDTO dishDTO1(){
        return DishDTO.builder()
                .dishId(1)
                .name(dishEntity1().getName())
                .price(dishEntity1().getPrice())
                .availability(dishEntity1().getAvailability())
                .isHidden(dishEntity1().getIsHidden())
                .build();
    }
    public static StatusListDTO statusListDTO1(){
        return StatusListDTO.builder()
                .statusId(1)
                .description("new")
                .build();
    }

    public static SupperOrderDTO supperOrderDTO1(){
        return SupperOrderDTO.builder()
                .orderId(1)
                .orderDateTime(String.valueOf(OffsetDateTime.of(2020, 12, 10, 12, 0, 0, 0, ZoneOffset.ofHours(0))))
                .clientDTO(clientDTO1())
                .restaurantDTO(restaurantDTO1())
                .statusListDTO(statusListDTO1())
                .build();
    }

    public static DishListDTO dishListDTO1(){
        return DishListDTO.builder()
                .dishDTO(dishDTO1())
                .supperOrderDTO(supperOrderDTO1())
                .quantity(1)
                .build();
    }

}
