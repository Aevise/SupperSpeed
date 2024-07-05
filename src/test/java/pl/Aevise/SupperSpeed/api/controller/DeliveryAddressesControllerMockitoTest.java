package pl.Aevise.SupperSpeed.api.controller;

import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.DeliveryAddressService;
import pl.Aevise.SupperSpeed.business.RestaurantService;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.util.Constants.*;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.deliveryAddressList1;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.deliveryAddressList2;

@ExtendWith(MockitoExtension.class)
class DeliveryAddressesControllerMockitoTest {

    @Mock
    private DeliveryAddressService deliveryAddressService;

    @Mock
    private AddressService addressService;

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private DeliveryAddressesController deliveryAddressesController;

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanGetDeliveryAddressesForRestaurant(
            int currAdrPage,
            String currAdrSortingDirection,
            int exiAdrPage,
            String exiAdrSortingDirection,
            PageRequest DALPage,
            PageRequest DAPage,
            String expected,
            List<DeliveryAddressDTO> addressesWithoutDelivery
    ) {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities("RESTAURANT").build();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        String postalCode = restaurantDTO.getAddress().getPostalCode();
        var deliveryAddressLists = new PageImpl<>(List.of(
                deliveryAddressList1(),
                deliveryAddressList2()
        ));
        var deliveryAddressDTOS = List.of(deliveryAddressDTO1(), deliveryAddressDTO2());
        var addressesWithoutDeliveryToTest = new PageImpl<>(addressesWithoutDelivery);


        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO1());
        when(addressService.getByRestaurantId(restaurantId)).thenReturn(restaurantDTO.getAddress());
        when(deliveryAddressService.getAllDeliveryAddressesByRestaurantId(restaurantId, DALPage))
                .thenReturn(deliveryAddressLists);
        when(deliveryAddressService.separateAddresses(deliveryAddressLists)).thenReturn(deliveryAddressDTOS);
        when(deliveryAddressService.getAddressesWithoutDeliveryBasedOnPostalCode(
                restaurantId, postalCode, DAPage)).thenReturn(addressesWithoutDeliveryToTest);

        String result = deliveryAddressesController.showDeliveryAddresses(userDetails, model, currAdrPage, currAdrSortingDirection, exiAdrPage, exiAdrSortingDirection);

        //then
        assertThat(result).isEqualTo(expected);
        assertThat(model.get("addresses")).isNotNull().isEqualTo(deliveryAddressDTOS);
        assertThat(model.get("restaurantAddress")).isNotNull().isEqualTo(restaurantDTO.getAddress());
        assertThat(model.get("addressesWithoutDelivery")).isNotNull().isEqualTo(addressesWithoutDeliveryToTest);
        assertThat(model.get("restaurantId")).isNotNull().isEqualTo(restaurantId);
        assertThat(model.get("totalNumberOfPages")).isNotNull().isEqualTo(deliveryAddressLists.getTotalPages());
        assertThat(model.get("CAPage")).isNotNull().isEqualTo(currAdrPage);
        assertThat(model.get("CASortingDirection")).isNotNull().isEqualTo(currAdrSortingDirection);
        assertThat(model.get("EASortingDirection")).isNotNull().isEqualTo(exiAdrSortingDirection);
        assertThat(model.get("EAPage")).isNotNull().isEqualTo(exiAdrPage);
    }

    private static Stream<Arguments> checkThatYouCanGetDeliveryAddressesForRestaurant() {
        return Stream.of(
                Arguments.of(0, "asc", 0, "asc",
                        PageRequest.of(0, 10, Sort.by("deliveryAddressEntity.streetName").ascending()),
                        PageRequest.of(0, 10, Sort.by("streetName").ascending()),
                        "delivery_addresses",
                        List.of(deliveryAddressDTO3(), deliveryAddressDTO4()),

                        0, "desc", 0, "asc",
                        PageRequest.of(0, 10, Sort.by("deliveryAddressEntity.streetName").ascending()),
                        PageRequest.of(0, 10, Sort.by("streetName").ascending()),
                        "delivery_addresses",
                        List.of(deliveryAddressDTO3(), deliveryAddressDTO4()),

                        0, "desc", 0, "desc",
                        PageRequest.of(0, 10, Sort.by("deliveryAddressEntity.streetName").ascending()),
                        PageRequest.of(0, 10, Sort.by("streetName").ascending()),
                        "delivery_addresses",
                        List.of(deliveryAddressDTO3(), deliveryAddressDTO4()),

                        0, "asc", 0, "desc",
                        PageRequest.of(0, 10, Sort.by("deliveryAddressEntity.streetName").ascending()),
                        PageRequest.of(0, 10, Sort.by("streetName").ascending()),
                        "delivery_addresses",
                        List.of(deliveryAddressDTO3(), deliveryAddressDTO4()),

                        0, "asc", 0, "desc",
                        PageRequest.of(0, 10, Sort.by("deliveryAddressEntity.streetName").ascending()),
                        PageRequest.of(0, 10, Sort.by("streetName").ascending()),
                        "delivery_addresses",
                        List.of(),

                        0, "asc", 0, "asc",
                        PageRequest.of(0, 10, Sort.by("deliveryAddressEntity.streetName").ascending()),
                        PageRequest.of(0, 10, Sort.by("streetName").ascending()),
                        "delivery_addresses",
                        List.of())
                        );
    }
}