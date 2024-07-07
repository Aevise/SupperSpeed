package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.ExtendedModelMap;
import pl.Aevise.SupperSpeed.api.dto.AddressDTO;
import pl.Aevise.SupperSpeed.api.dto.RestaurantDTO;
import pl.Aevise.SupperSpeed.api.dto.mapper.AddressMapper;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.ImageHandlingService;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.domain.Address;

import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_LOGO_HEIGHT;
import static pl.Aevise.SupperSpeed.business.utils.ImageHandlerInterface.MAX_LOGO_WIDTH;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;
import static pl.Aevise.SupperSpeed.util.POJOFixtures.address1;

@ExtendWith(MockitoExtension.class)
class RestaurantProfileControllerMockitoTest {

    @Mock
    private RestaurantService restaurantService;
    @Mock
    private AddressService addressService;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private ImageHandlingService imageHandlingService;

    @InjectMocks
    private RestaurantProfileController restaurantProfileController;

    @Test
    void checkThatExceptionIsThrownWhenAddressDoesNotExist() {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities("RESTAURANT").build();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        int restaurantAddressId = restaurantDTO.getAddress().getAddressId();
        String expectedErrorMessage = "Could not find restaurant address with id: [%s]".formatted(restaurantAddressId);

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO);
        when(addressService.findById(restaurantAddressId)).thenReturn(Optional.empty());

        NoSuchElementException expectedException = assertThrows(NoSuchElementException.class,
                () -> restaurantProfileController.getRestaurantProfile(model, userDetails));

        //then
        assertEquals(expectedErrorMessage, expectedException.getMessage());
    }

    @Test
    void checkThatYouCanGetRestaurantProfileWithoutImage() throws IOException {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities("RESTAURANT").build();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        int restaurantId = restaurantDTO.getRestaurantId();
        int restaurantAddressId = restaurantDTO.getAddress().getAddressId();
        Address address = address1();
        AddressDTO addressDTO = addressDTO1();

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO);
        when(addressService.findById(restaurantAddressId)).thenReturn(Optional.of(address));
        when(addressMapper.mapToDTO(address)).thenReturn(addressDTO);

        String result = restaurantProfileController.getRestaurantProfile(model, userDetails);

        //then
        assertThat(result).isNotNull().isEqualTo("restaurant_profile");
        assertThat(model.get("restaurantDTO")).isNotNull().isEqualTo(restaurantDTO);
        assertThat(model.get("addressDTO")).isNotNull().isEqualTo(addressDTO);
        assertThat(model.get("restaurantId")).isNotNull().isEqualTo(restaurantId);
    }

    @Test
    void checkThatYouCanGetRestaurantProfileWithImage() throws IOException {
        //given
        UserDetails userDetails = User.withUsername(TEST_RESTAURANT_EMAIL_1).password(testPassword).authorities("RESTAURANT").build();
        RestaurantDTO restaurantDTO = restaurantDTO1();
        restaurantDTO.setImageDTO(imageDTO1());
        String restaurantName = restaurantDTO.getRestaurantName();
        int restaurantId = restaurantDTO.getRestaurantId();
        int restaurantAddressId = restaurantDTO.getAddress().getAddressId();
        Address address = address1();
        AddressDTO addressDTO = addressDTO1();
        String restaurantDirectory = "testName";

        //when
        ExtendedModelMap model = new ExtendedModelMap();

        when(restaurantService.findRestaurantByEmail(TEST_RESTAURANT_EMAIL_1)).thenReturn(restaurantDTO);
        when(addressService.findById(restaurantAddressId)).thenReturn(Optional.of(address));
        when(addressMapper.mapToDTO(address)).thenReturn(addressDTO);
        when(imageHandlingService.getRestaurantName(restaurantId, restaurantName)).thenReturn(restaurantDirectory);

        String result = restaurantProfileController.getRestaurantProfile(model, userDetails);

        //then
        assertThat(result).isNotNull().isEqualTo("restaurant_profile");
        assertThat(model.get("restaurantDTO")).isNotNull().isEqualTo(restaurantDTO);
        assertThat(model.get("addressDTO")).isNotNull().isEqualTo(addressDTO);
        assertThat(model.get("restaurantId")).isNotNull().isEqualTo(restaurantId);
        assertThat(model.get("imageName")).isNotNull().isEqualTo(restaurantDTO.getImageDTO().getImageURL());
        assertThat(model.get("restaurantDirectory")).isNotNull().isEqualTo(restaurantDirectory);
        assertThat(model.get("logoWidth")).isNotNull().isEqualTo(MAX_LOGO_WIDTH);
        assertThat(model.get("logoHeight")).isNotNull().isEqualTo(MAX_LOGO_HEIGHT);
    }
}