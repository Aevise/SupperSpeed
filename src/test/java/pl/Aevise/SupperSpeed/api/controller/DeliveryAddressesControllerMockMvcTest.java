package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import pl.Aevise.SupperSpeed.business.AddressService;
import pl.Aevise.SupperSpeed.business.DeliveryAddressService;
import pl.Aevise.SupperSpeed.business.RestaurantService;

import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;

@WebMvcTest(controllers = CreateAccountController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
class DeliveryAddressesControllerMockMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private DeliveryAddressService deliveryAddressService;
    @MockBean
    private AddressService addressService;
    @MockBean
    private RestaurantService restaurantService;

    @Test
    void showDeliveryAddresses() {
    }

    @Test
    void removeDeliveryAddress() {
    }

    @Test
    void addDeliveryAddress() {
    }
}