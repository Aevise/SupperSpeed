package pl.Aevise.SupperSpeed.api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import pl.Aevise.SupperSpeed.business.RestaurantResponseService;
import pl.Aevise.SupperSpeed.business.UserRatingService;

@WebMvcTest
class CommentsControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private UserRatingService userRatingService;
    @MockBean
    private RestaurantResponseService restaurantResponseService;
    @Test
    void addUserCommentToOrder() {
    }

    @Test
    void addRestaurantResponseToComment() {
    }
}