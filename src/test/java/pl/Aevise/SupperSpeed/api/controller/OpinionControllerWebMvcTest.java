package pl.Aevise.SupperSpeed.api.controller;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import pl.Aevise.SupperSpeed.api.controller.utils.interfaces.PageRequestUtils;
import pl.Aevise.SupperSpeed.api.dto.*;
import pl.Aevise.SupperSpeed.business.RestaurantService;
import pl.Aevise.SupperSpeed.business.UserRatingService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.Aevise.SupperSpeed.api.controller.OpinionController.OPINION;
import static pl.Aevise.SupperSpeed.util.Constants.TEST_CLIENT_EMAIL_1;
import static pl.Aevise.SupperSpeed.util.DTOFixtures.*;

@WebMvcTest(controllers = OpinionController.class)
@AllArgsConstructor(onConstructor = @__(@Autowired))
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@WithMockUser(username = TEST_CLIENT_EMAIL_1, authorities = "CLIENT")
class OpinionControllerWebMvcTest {

    private MockMvc mockMvc;

    @MockBean
    private PageRequestUtils pageRequestUtils;

    @MockBean
    private UserRatingService userRatingService;
    @MockBean
    private RestaurantService restaurantService;


    @Test
    void checkThatYouCanGetRestaurantOpinionPageOfRestaurantWithNoOpinions() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("orderId").ascending());
        Page<OpinionDTO> noOpinions = new PageImpl<>(List.of(), pageRequest, 1);
        var totalRestaurantRatingDTO = TotalRestaurantRatingDTO.builder()
                .restaurantId(restaurantId)
                .amountOfRatedOrders(0)
                .deliveryRating(0.0)
                .foodRating(0.0)
                .build();
        String expectedPartOfHTML = "<div class=\"col text-center font-weight-bold display-4\">Restaurant " + restaurantDTO.getRestaurantName() + " has not been rated yet!</div>";

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.putIfAbsent("dir", "asc");
        parametersMap.putIfAbsent("page", "0");
        parametersMap.forEach(params::add);

        //when
        when(pageRequestUtils.buildPageRequestForRatedOrders(anyString(), anyInt())).thenReturn(pageRequest);
        when(restaurantService.findRestaurantDTOById(restaurantId)).thenReturn(restaurantDTO);
        when(userRatingService.getOpinionsAboutOrdersFromRestaurant(anyInt(), any(PageRequest.class))).thenReturn(noOpinions);
        when(userRatingService.getRestaurantRating(restaurantId)).thenReturn(totalRestaurantRatingDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(OPINION)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attributeExists("totalRating"))
                .andExpect(model().attributeExists("currDir"))
                .andExpect(model().attributeExists("currPage"))
                .andExpect(view().name("opinion"));

        assertThat(content).contains(expectedPartOfHTML);
    }

    @Test
    void checkThatYouCanSeeOpinionsAboutRestaurant() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("orderId").ascending());
        OpinionDTO opinionDTO = opinionDTO1();
        UserRatingDTO userRatingDTO = userRatingDTO1();
        opinionDTO.setUserRatingDTO(userRatingDTO);
        var totalRestaurantRatingDTO = TotalRestaurantRatingDTO.builder()
                .restaurantId(restaurantId)
                .amountOfRatedOrders(1)
                .deliveryRating(Double.valueOf(userRatingDTO.getDeliveryRating()))
                .foodRating(Double.valueOf(userRatingDTO.getFoodRating()))
                .build();

        Page<OpinionDTO> opinions = new PageImpl<>(List.of(opinionDTO), pageRequest, 1);
        List<String> expectedPartsOfHTML = List.of(
                "<div class=\"col text-center font-weight-bold display-4\">Opinions about " + restaurantDTO.getRestaurantName() + ":</div>",
                "<div class=\"col\">Total delivery rating: " + String.format("%.2f", totalRestaurantRatingDTO.getDeliveryRating()).replace(",", ".") + "</div>",
                "<div class=\"col\">Total food rating: " + String.format("%.2f", totalRestaurantRatingDTO.getFoodRating()).replace(",", ".") + "</div>",
                "<div class=\"col\">Total amount of rated orders: " + totalRestaurantRatingDTO.getAmountOfRatedOrders() + "</div>",
                "<div class=\"container\"><b>User Rating</b>"
        );

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.putIfAbsent("dir", "asc");
        parametersMap.putIfAbsent("page", "0");
        parametersMap.forEach(params::add);

        //when
        when(pageRequestUtils.buildPageRequestForRatedOrders(anyString(), anyInt())).thenReturn(pageRequest);
        when(restaurantService.findRestaurantDTOById(restaurantId)).thenReturn(restaurantDTO);
        when(userRatingService.getOpinionsAboutOrdersFromRestaurant(restaurantId, pageRequest)).thenReturn(opinions);
        when(userRatingService.getRestaurantRating(restaurantId)).thenReturn(totalRestaurantRatingDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(OPINION)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attributeExists("totalRating"))
                .andExpect(model().attributeExists("currDir"))
                .andExpect(model().attributeExists("currPage"))
                .andExpect(view().name("opinion"));

        assertThat(content).contains(expectedPartsOfHTML);
    }

    @Test
    void checkThatYouCanSeeOpinionsAboutRestaurantWithRestaurantResponse() throws Exception {
        //given
        RestaurantDTO restaurantDTO = restaurantDTO1();
        Integer restaurantId = restaurantDTO.getRestaurantId();
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("orderId").ascending());
        OpinionDTO opinionDTO = opinionDTO1();
        RestaurantResponseDTO restaurantResponseDTO = restaurantResponseDTO1();
        UserRatingDTO userRatingDTO = userRatingDTO1();
        userRatingDTO.setRestaurantResponseDTO(restaurantResponseDTO);
        opinionDTO.setUserRatingDTO(userRatingDTO);
        var totalRestaurantRatingDTO = TotalRestaurantRatingDTO.builder()
                .restaurantId(restaurantId)
                .amountOfRatedOrders(1)
                .deliveryRating(Double.valueOf(userRatingDTO.getDeliveryRating()))
                .foodRating(Double.valueOf(userRatingDTO.getFoodRating()))
                .build();

        Page<OpinionDTO> opinions = new PageImpl<>(List.of(opinionDTO), pageRequest, 1);
        List<String> expectedPartsOfHTML = List.of(
                "<div class=\"col text-center font-weight-bold display-4\">Opinions about " + restaurantDTO.getRestaurantName() + ":</div>",
                "<div class=\"col\">Total delivery rating: " + String.format("%.2f", totalRestaurantRatingDTO.getDeliveryRating()).replace(",", ".") + "</div>",
                "<div class=\"col\">Total food rating: " + String.format("%.2f", totalRestaurantRatingDTO.getFoodRating()).replace(",", ".") + "</div>",
                "<div class=\"col\">Total amount of rated orders: " + totalRestaurantRatingDTO.getAmountOfRatedOrders() + "</div>",
                "<div class=\"container\"><b>User Rating</b>",
                "<b>Restaurant Response:</b>",
                "<div class=\"col\">Response: " + restaurantResponseDTO.getDescription() + "</div>"
        );

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        Map<String, String> parametersMap = new HashMap<>();
        parametersMap.putIfAbsent("restaurantId", restaurantId.toString());
        parametersMap.putIfAbsent("dir", "asc");
        parametersMap.putIfAbsent("page", "0");
        parametersMap.forEach(params::add);

        //when
        when(pageRequestUtils.buildPageRequestForRatedOrders(anyString(), anyInt())).thenReturn(pageRequest);
        when(restaurantService.findRestaurantDTOById(restaurantId)).thenReturn(restaurantDTO);
        when(userRatingService.getOpinionsAboutOrdersFromRestaurant(restaurantId, pageRequest)).thenReturn(opinions);
        when(userRatingService.getRestaurantRating(restaurantId)).thenReturn(totalRestaurantRatingDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get(OPINION)
                .params(params)
                .with(SecurityMockMvcRequestPostProcessors.csrf()));
        String content = result.andReturn().getResponse().getContentAsString();

        //then
        result
                .andExpect(status().isOk())
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("restaurant"))
                .andExpect(model().attributeExists("totalRating"))
                .andExpect(model().attributeExists("currDir"))
                .andExpect(model().attributeExists("currPage"))
                .andExpect(view().name("opinion"));

        assertThat(content).contains(expectedPartsOfHTML);
    }
}