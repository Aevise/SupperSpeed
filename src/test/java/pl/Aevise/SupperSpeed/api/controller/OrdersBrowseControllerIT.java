package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.OrdersBrowseController.SUPPER_SPEED_ORDERS_BROWSER;
import static pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles.CLIENT;
import static pl.Aevise.SupperSpeed.infrastructure.security.utils.AvailableRoles.RESTAURANT;
import static pl.Aevise.SupperSpeed.util.Constants.*;

@AutoConfigureMockMvc
class OrdersBrowseControllerIT extends AbstractITConfiguration {

    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource
    void checkThatYouCanGetYourOrders(
            String username,
            String authority,
            int userId
    ) throws Exception {
        //given
        UserDetails userDetails = User.withUsername(username).password(testPassword).authorities(authority).build();

        //when
        ResultActions result = mockMvc.perform(get(SUPPER_SPEED_ORDERS_BROWSER)
                .with(SecurityMockMvcRequestPostProcessors.user(userDetails)));

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("statusListDTO"))
                .andExpect(model().attributeExists("orders"))
                .andExpect(model().attributeExists("role"))
                .andExpect(model().attributeExists("dishesByOrderId"))
                .andExpect(model().attributeExists("ordersTotalPrice"))
                .andExpect(model().attributeExists("userId"))
                .andExpect(view().name("orders_page"));
    }

    public static Stream<Arguments> checkThatYouCanGetYourOrders() {
        return Stream.of(
                Arguments.of(TEST_CLIENT_EMAIL_FLYWAY_1, CLIENT.name(), 4),
                Arguments.of(TEST_RESTAURANT_EMAIL_FLYWAY_1, RESTAURANT.name(), 3),
                Arguments.of(TEST_RESTAURANT_EMAIL_FLYWAY_1, RESTAURANT.name(), 2)
        );
    }
}