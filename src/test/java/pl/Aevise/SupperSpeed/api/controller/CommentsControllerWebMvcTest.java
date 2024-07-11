package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import pl.Aevise.SupperSpeed.api.dto.RestaurantResponseDTO;
import pl.Aevise.SupperSpeed.api.dto.UserRatingDTO;
import pl.Aevise.SupperSpeed.business.RestaurantResponseService;
import pl.Aevise.SupperSpeed.business.UserRatingService;

import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.CommentsController.ADD_COMMENT;
import static pl.Aevise.SupperSpeed.api.controller.CommentsController.ADD_RESPONSE;
import static pl.Aevise.SupperSpeed.api.controller.OrdersBrowseController.SUPPER_SPEED_ORDERS_BROWSER;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_RESTAURANT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;

@WebMvcTest(controllers = CommentsController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class CommentsControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private UserRatingService userRatingService;
    @MockBean
    private RestaurantResponseService restaurantResponseService;

    @Test
    public void checkThatYouGetErrorWhenYouTryToAddCommentWithoutAnyAuthorities() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = userRatingDTO1().asMap();
        Integer orderId = supperOrderDTO1().getOrderId();
        parametersMap.putIfAbsent("orderId", orderId.toString());
        parametersMap.forEach(parameters::add);

        // when
        ResultActions perform = mockMvc.perform(post(ADD_COMMENT).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(TEST_CLIENT_EMAIL_1).authorities()));

        //then
        perform
                .andExpect(status().isUnauthorized())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    public void checkThatYouCannotAddCommentToOrderWithWrongRole() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = userRatingDTO1().asMap();
        Integer orderId = supperOrderDTO1().getOrderId();
        parametersMap.putIfAbsent("orderId", orderId.toString());
        parametersMap.forEach(parameters::add);

        // when
        ResultActions perform = mockMvc.perform(post(ADD_COMMENT).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform
                .andExpect(status().isUnauthorized())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    public void checkThatYouCanAddCommentToTheOrderAsTheUser() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = userRatingDTO1().asMap();
        Integer orderId = supperOrderDTO1().getOrderId();
        parametersMap.putIfAbsent("orderId", orderId.toString());
        parametersMap.forEach(parameters::add);

        // when
        doNothing().when(userRatingService).saveNewComment(any(UserRatingDTO.class), anyInt());

        ResultActions perform = mockMvc.perform(post(ADD_COMMENT).params(parameters).with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("redirect:" + SUPPER_SPEED_ORDERS_BROWSER));
    }

    @Test
    public void checkThatYouGetErrorWhenYouTryToAddRestaurantResponseWithoutAnyAuthorities() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantResponseDTO1().asMap();
        Integer userRatingId = userRatingDTO1().getUserRatingId();
        parametersMap.putIfAbsent("userRatingId", userRatingId.toString());
        parametersMap.forEach(parameters::add);

        // when
        ResultActions perform = mockMvc.perform(post(ADD_RESPONSE).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .with(SecurityMockMvcRequestPostProcessors.user(TEST_RESTAURANT_EMAIL_1).authorities()));

        //then
        perform
                .andExpect(status().isUnauthorized())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
    public void checkThatYouCanNotAddRestaurantResponseAsClient() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantResponseDTO1().asMap();
        Integer userRatingId = userRatingDTO1().getUserRatingId();
        parametersMap.putIfAbsent("userRatingId", userRatingId.toString());
        parametersMap.forEach(parameters::add);

        // when
        ResultActions perform = mockMvc.perform(post(ADD_RESPONSE).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform
                .andExpect(status().isUnauthorized())
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(view().name("error"));
    }

    @Test
    @WithMockUser(username = TEST_RESTAURANT_EMAIL_1, authorities = "RESTAURANT")
    public void checkThatYouCanAddRestaurantResponseAsRestaurant() throws Exception {
        //given
        LinkedMultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = restaurantResponseDTO1().asMap();
        Integer userRatingId = userRatingDTO1().getUserRatingId();
        parametersMap.putIfAbsent("userRatingId", userRatingId.toString());
        parametersMap.forEach(parameters::add);

        // when
        doNothing().when(restaurantResponseService).saveRestaurantResponse(any(RestaurantResponseDTO.class), anyInt());

        ResultActions perform = mockMvc.perform(post(ADD_RESPONSE).params(parameters)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));

        //then
        perform
                .andExpect(status().is3xxRedirection())
                .andExpect(model().attributeDoesNotExist("errorMessage"))
                .andExpect(view().name("redirect:" + SUPPER_SPEED_ORDERS_BROWSER));
    }
}