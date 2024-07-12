package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.DeliveryAddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.DeliveryAddressService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.DeliveryAddressList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.DeliveryAddressesController.REMOVE_DELIVERY_ADDRESS;
import static pl.Aevise.SupperSpeed.api.controller.DeliveryAddressesController.SHOW_DELIVERY_ADDRESSES;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.deliveryAddressList1;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.deliveryAddressList2;

@WebMvcTest(controllers = DeliveryAddressesController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
class DeliveryAddressesControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private DeliveryAddressService deliveryAddressService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private RestaurantService restaurantService;

    @ParameterizedTest
    @MethodSource
    void checkThatYouCanGetDeliveryAddresses(
            Integer currAdrPage,
            String currAdrSortingDir,
            Integer exiAdrPage,
            String exiAdrSortingDirection
    ) throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        AddressDTO addressDTO = addressDTO1();

        PageRequest pageRequestCurrAdr;
        if (currAdrSortingDir.equals("asc")) {
            pageRequestCurrAdr = PageRequest.of(currAdrPage, 10, Sort.by("deliveryAddressEntity.streetName").ascending());
        } else {
            pageRequestCurrAdr = PageRequest.of(currAdrPage, 10, Sort.by("deliveryAddressEntity.streetName").descending());
        }
        Page<DeliveryAddressList> allDeliveryAddressesListForRestaurant = new PageImpl<>(List.of(
                deliveryAddressList1(), deliveryAddressList2()
        ), pageRequestCurrAdr, 1);
        List<DeliveryAddressDTO> deliveryAddresses = List.of(
                deliveryAddressDTO1(), deliveryAddressDTO2(), deliveryAddressDTO3()
        );

        PageRequest pageRequestExiAdr;
        if (exiAdrSortingDirection.equals("asc")) {
            pageRequestExiAdr = PageRequest.of(exiAdrPage, 10, Sort.by("streetName").ascending());
        } else {
            pageRequestExiAdr = PageRequest.of(exiAdrPage, 10, Sort.by("streetName").descending());
        }
        Page<DeliveryAddressDTO> addressesWithoutDelivery = new PageImpl<>(List.of(
                deliveryAddressDTO3()
        ), pageRequestExiAdr, 1);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("ca-page", currAdrPage.toString());
        parametersMap.putIfAbsent("ca-dir", currAdrSortingDir);
        parametersMap.putIfAbsent("ea-page", exiAdrPage.toString());
        parametersMap.putIfAbsent("ea-dir", exiAdrSortingDirection);
        parametersMap.forEach(params::add);


        //when
        when(restaurantService.findRestaurantByEmail(anyString())).thenReturn(restaurantDTO);
        when(addressService.getByRestaurantId(restaurantId)).thenReturn(addressDTO);
        when(deliveryAddressService.getAllDeliveryAddressesByRestaurantId(restaurantId, pageRequestCurrAdr)).thenReturn(allDeliveryAddressesListForRestaurant);
        when(deliveryAddressService.separateAddresses(allDeliveryAddressesListForRestaurant)).thenReturn(deliveryAddresses);
        when(deliveryAddressService.getAddressesWithoutDeliveryBasedOnPostalCode(restaurantId, addressDTO.getPostalCode(), pageRequestExiAdr)).thenReturn(addressesWithoutDelivery);

        ResultActions perform = mockMvc.perform(get(SHOW_DELIVERY_ADDRESSES).params(params).with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform
                .andExpect(status().isOk())
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(model().attributeExists("addresses"))
                .andExpect(model().attribute("addresses", deliveryAddresses))
                .andExpect(model().attributeExists("restaurantAddress"))
                .andExpect(model().attribute("restaurantAddress", addressDTO))
                .andExpect(model().attributeExists("addressesWithoutDelivery"))
                .andExpect(model().attribute("addressesWithoutDelivery", addressesWithoutDelivery))
                .andExpect(model().attributeExists("restaurantId"))
                .andExpect(model().attribute("restaurantId", restaurantId))
                .andExpect(model().attributeExists("totalNumberOfPages"))
                .andExpect(model().attribute("totalNumberOfPages", allDeliveryAddressesListForRestaurant.getTotalPages()))
                .andExpect(model().attributeExists("CAPage"))
                .andExpect(model().attribute("CAPage", currAdrPage))
                .andExpect(model().attributeExists("CASortingDirection"))
                .andExpect(model().attribute("CASortingDirection", currAdrSortingDir))
                .andExpect(model().attributeExists("EASortingDirection"))
                .andExpect(model().attribute("EASortingDirection", exiAdrSortingDirection))
                .andExpect(model().attributeExists("EAPage"))
                .andExpect(model().attribute("EAPage", exiAdrPage));

    }

    public static Stream<Arguments> checkThatYouCanGetDeliveryAddresses() {
        return Stream.of(
                Arguments.of("0", "asc", "0", "asc"),
                Arguments.of("0", "asc", "0", "desc"),
                Arguments.of("0", "desc", "0", "asc"),
                Arguments.of("0", "desc", "0", "desc")
        );
    }

    @Test
    void checkThatYouCanRemoveDeliveryAddress() throws Exception {
        //given
        Integer deliveryAddressId = deliveryAddressDTO1().getDeliveryAddressId();
        Integer restaurantId = restaurantDTO1().getRestaurantId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("deliveryAddressId", deliveryAddressId.toString());
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.forEach(params::add);

        //when
        doNothing().when(deliveryAddressService).removeDeliveryAddress(deliveryAddressId, restaurantId);

        ResultActions result = mockMvc.perform(post(REMOVE_DELIVERY_ADDRESS)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result.andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + SHOW_DELIVERY_ADDRESSES));
    }

    @Test
    void checkThatYouCanNotRemoveDeliveryAddressWithoutRestaurantAuthority() throws Exception {
        //given
        Integer deliveryAddressId = deliveryAddressDTO1().getDeliveryAddressId();
        Integer restaurantId = restaurantDTO1().getRestaurantId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("deliveryAddressId", deliveryAddressId.toString());
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.forEach(params::add);

        //when
        doNothing().when(deliveryAddressService).removeDeliveryAddress(deliveryAddressId, restaurantId);

        ResultActions result = mockMvc.perform(post(REMOVE_DELIVERY_ADDRESS)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(TEST_CLIENT_EMAIL_1).authorities()));

        //then
        result.andExpect(status().isNotAcceptable())
                .andExpect(view().name("error"));
    }

    @Test
    void checkThatYouCanAddDeliveryAddress() throws Exception {
        //given
        DeliveryAddressDTO deliveryAddressDTO = deliveryAddressDTO1();
        Integer restaurantId = restaurantDTO1().getRestaurantId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = deliveryAddressDTO.asMap();
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.forEach(params::add);

        //when
        doNothing().when(deliveryAddressService).addDeliveryAddress(deliveryAddressDTO, restaurantId);

        ResultActions result = mockMvc.perform(post(REMOVE_DELIVERY_ADDRESS)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        result.andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + SHOW_DELIVERY_ADDRESSES));
    }

    @Test
    void checkThatYouCanNotAddDeliveryAddressWithoutRestaurantAuthority() throws Exception {
        //given
        DeliveryAddressDTO deliveryAddressDTO = deliveryAddressDTO1();
        Integer restaurantId = restaurantDTO1().getRestaurantId();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = deliveryAddressDTO.asMap();
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.forEach(params::add);

        //when
        doNothing().when(deliveryAddressService).addDeliveryAddress(deliveryAddressDTO, restaurantId);

        ResultActions result = mockMvc.perform(post(REMOVE_DELIVERY_ADDRESS)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(TEST_CLIENT_EMAIL_1).authorities()));

        //then
        result.andExpect(status().isNotAcceptable())
                .andExpect(view().name("error"));
    }

}