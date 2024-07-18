package pl.Aevise.SupperSpeed.api.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.RestaurantResponseDTO;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.RestaurantResponseEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.entity.UserRatingEntity;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.RestaurantResponseJpaRepository;
import pl.Aevise.SupperSpeed.infrastructure.database.repository.jpa.UserRatingJpaRepository;
import pl.Aevise.SupperSpeed.integration.configuration.AbstractITConfiguration;
import pl.Aevise.SupperSpeed.util.DTOFixtures;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.CommentsController.ADD_COMMENT;
import static pl.Aevise.SupperSpeed.api.controller.CommentsController.ADD_RESPONSE;
import static pl.Aevise.SupperSpeed.api.controller.OrdersBrowseController.SUPPER_SPEED_ORDERS_BROWSER;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_FLYWAY_1;
import static pl.Aevise.SupperSpeed.util.Constants.testPassword;

@AutoConfigureMockMvc
class CommentsControllerIT extends AbstractITConfiguration {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRatingJpaRepository userRatingJpaRepository;
    @Autowired
    private RestaurantResponseJpaRepository restaurantResponseJpaRepository;

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_FLYWAY_1, password = testPassword, authorities = "CLIENT")
    void checkThatYouCanAddUserResponseToFinishedOrderAsClient() throws Exception {
        //given
        int orderId = 2;
        UserRatingDTO userRatingDTO = DTOFixtures.userRatingDTO1();
        userRatingDTO.setUserRatingId(null);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = userRatingDTO.asMap();
        parametersMap.putIfAbsent("orderId", Integer.toString(orderId));
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(post(ADD_COMMENT)
                .params(params));
        UserRatingEntity newUserRating = userRatingJpaRepository.findById(2).orElseThrow(
                () -> new EntityNotFoundException("User Rating not added")
        );

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + SUPPER_SPEED_ORDERS_BROWSER));

        assertEquals(newUserRating.getUserRatingId(), 2);
        assertEquals(newUserRating.getDeliveryRating(), userRatingDTO.getDeliveryRating());
        assertEquals(newUserRating.getDescription(), userRatingDTO.getDescription());
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_FLYWAY_1, password = testPassword, authorities = "RESTAURANT")
    void addRestaurantResponseToComment() throws Exception {
        //given
        int userRatingId = 1;
        RestaurantResponseDTO restaurantResponseDTO = DTOFixtures.restaurantResponseDTO1();
        restaurantResponseDTO.setRestaurantResponseId(null);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantResponseDTO.asMap();
        parametersMap.putIfAbsent("userRatingId", Integer.toString(userRatingId));
        parametersMap.forEach(params::add);

        //when
        ResultActions result = mockMvc.perform(post(ADD_RESPONSE)
                .params(params));
        RestaurantResponseEntity newRestaurantRating = restaurantResponseJpaRepository.findById(1).orElseThrow(
                () -> new EntityNotFoundException("Restaurant Rating not added"));

        //then
        result
                .andExpect(status().isFound())
                .andExpect(model().hasNoErrors())
                .andExpect(view().name("redirect:" + SUPPER_SPEED_ORDERS_BROWSER));

        assertEquals(newRestaurantRating.getRestaurantResponseId(), 1);
        assertEquals(newRestaurantRating.getDescription(), restaurantResponseDTO.getDescription());
    }
}